document.addEventListener('DOMContentLoaded', () => {
  const urlInput = document.getElementById('url-input');
  const addDownloadButton = document.getElementById('add-download');
  const downloadContainer = document.getElementById('downloads');
  let downloadCount = 0;
  const workers = {}; // To store active workers by download ID

  // Fetch strings from the JSON file
  let strings;
  fetch('strings.json')
    .then(response => response.json())
    .then(data => {
      strings = data;

      // Set button and placeholder text from the JSON
      addDownloadButton.innerText = strings.addDownloadButton;
      urlInput.placeholder = strings.urlInputPlaceholder;
      document.getElementById('app-title').innerText = strings.appTitle;
    })
    .catch(error => log('Error loading strings:', error));

  // Log function to suppress logs in production
  function log(...args) {
    if (process.env.NODE_ENV !== 'production') {
      console.log(...args);
    }
  }

  // Event listener for adding a new download
  addDownloadButton.addEventListener('click', () => {
    const url = urlInput.value.trim();

    if (!url) {
      alert(strings.invalidUrlMessage);
      return;
    }

    downloadCount++;
    const id = `download-${downloadCount}`;
    createDownloadItem(url, id);
    startDownload(url, id);

    urlInput.value = '';
  });

  // Function to create a new download item in the UI
  function createDownloadItem(url, id) {
    const downloadItem = document.createElement('div');
    downloadItem.className = 'download-item';
    downloadItem.id = id;

    downloadItem.innerHTML = `
      <div class="download-header">
        <span>${url}</span>
        <div class="controls">
          <button id="pause-${id}">${strings.pauseButton}</button>
          <button id="resume-${id}" disabled>${strings.resumeButton}</button>
        </div>
      </div>
      <div class="progress-bar" id="progress-${id}"></div>
    `;

    downloadContainer.appendChild(downloadItem);

    const pauseButton = document.getElementById(`pause-${id}`);
    const resumeButton = document.getElementById(`resume-${id}`);

    pauseButton.addEventListener('click', () => pauseDownload(id));
    resumeButton.addEventListener('click', () => resumeDownload(id));
  }

  // Function to start a download
  function startDownload(url, id) {
    const worker = new Worker('worker.js');
    workers[id] = worker;

    const progressBar = document.getElementById(`progress-${id}`);
    const pauseButton = document.getElementById(`pause-${id}`);
    const resumeButton = document.getElementById(`resume-${id}`);

    worker.postMessage({ action: 'start', url });

    worker.onmessage = (event) => {
      const { type, progress, blob, error } = event.data;

      if (type === 'progress') {
        // Update the progress bar according to the download progress
        progressBar.style.width = `${progress}%`;
      } else if (type === 'completed') {
        saveFile(blob, url);
        progressBar.style.width = '100%';  // Ensure progress bar is full
        worker.terminate();
        delete workers[id];
        pauseButton.disabled = true;
        resumeButton.disabled = true;
      } else if (type === 'error') {
        log(`${strings.downloadFailedMessage} ${id}:`, error);
        worker.terminate();
        delete workers[id];
      }
    };
  }

  // Pause the download
  function pauseDownload(id) {
    const worker = workers[id];
    if (worker) {
      worker.postMessage({ action: 'pause' });
      document.getElementById(`pause-${id}`).disabled = true;
      document.getElementById(`resume-${id}`).disabled = false;
    }
  }

  // Resume the download
  function resumeDownload(id) {
    const worker = workers[id];
    if (worker) {
      worker.postMessage({ action: 'resume' });
      document.getElementById(`pause-${id}`).disabled = false;
      document.getElementById(`resume-${id}`).disabled = true;
    }
  }

  // Save the downloaded file
  function saveFile(blob, url) {
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = url.split('/').pop();
    document.body.appendChild(link);
    link.click();
    link.remove();
  }

  // Listen for changes in network status
  window.addEventListener('offline', () => {
    log(strings.offlineMessage);
    for (let id in workers) {
      alert(strings.offlineDownloadPausedMessage);
      document.getElementById(`pause-${id}`).disabled = true;
      document.getElementById(`resume-${id}`).disabled = true;
      workers[id].postMessage({ action: 'pause' });
    }
  });

  window.addEventListener('online', () => {
    for (let id in workers) {
      workers[id].postMessage({ action: 'resume' });
      document.getElementById(`pause-${id}`).disabled = false;
      document.getElementById(`resume-${id}`).disabled = true;
    }
  });
});
