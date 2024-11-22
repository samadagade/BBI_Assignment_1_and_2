let downloadState = {
  paused: false,
  url: null,
  position: 0,
  chunks: [],
  totalSize: 0,
};

self.onmessage = async (event) => {
  const { action, url } = event.data;

  if (action === 'start') {
    downloadState.url = url;
    downloadState.paused = false;
    await fetchAndDownload();
  } else if (action === 'pause') {
    downloadState.paused = true;
  } else if (action === 'resume') {
    downloadState.paused = false;
    await fetchAndDownload();
  }
};

async function fetchAndDownload() {
  if (downloadState.paused) return;

  try {
    const response = await fetch(downloadState.url, {
      headers: downloadState.position > 0 ? { Range: `bytes=${downloadState.position}-` } : undefined,
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch: ${response.statusText}`);
    }

    // Set total size only on the first request
    if (downloadState.totalSize === 0) {
      const contentLength = response.headers.get('content-length');
      const contentRange = response.headers.get('content-range');
      downloadState.totalSize = contentRange
        ? parseInt(contentRange.split('/')[1], 10)
        : parseInt(contentLength, 10);
    }

    const reader = response.body.getReader();

    while (!downloadState.paused) {
      const { done, value } = await reader.read();
      if (done) break;

      downloadState.chunks.push(value);
      downloadState.position += value.length;

      const progress = Math.round((downloadState.position / downloadState.totalSize) * 100);
      self.postMessage({ type: 'progress', progress });
    }

    if (downloadState.position === downloadState.totalSize) {
      const blob = new Blob(downloadState.chunks);
      self.postMessage({ type: 'completed', blob });
    }
  } catch (error) {
    self.postMessage({ type: 'error', error: error.message });
  }
}
