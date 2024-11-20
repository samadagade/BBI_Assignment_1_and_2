document.addEventListener("DOMContentLoaded", () => {   //execute JavaScript code only after initial HTML document has been completely loaded and parsed not depend load css
    const urlInput = document.getElementById("urlInput");            
    const addButton = document.getElementById("addButton");          //button to add URL
    const downloadList = document.getElementById("downloadList");    //list of items we downloding currently

    const workers = new Map();   //creating map to track an active workers

    const downloadStates = new Map();  //is used to manage and track the state of each download.
  
    addButton.addEventListener("click", () => {
      const url = urlInput.value.trim();  //retrieve current text in input field and remove trailing whitespaces using trim()

      if (!url) return alert("Please enter a valid URL.");  //if url is invalid dialog box and ok button
  
      if (workers.has(url)) {                              //if worker already download from url
        alert("Download for this URL already exists.");
        return;
      }
  
      const listItem = createDownloadItem(url);   //creading download item
      downloadList.appendChild(listItem);         //add downloading item in list 
  
      const worker = new Worker("worker.js");          //creating worker
      workers.set(url, worker);                       //add worker in map with key : url and value : worker

      downloadStates.set(url, { paused: false, receivedLength: 0, chunks: [] });  //add key : url and value : paused, receivedLength, chunks[] meaning stores data received so far
  
      worker.postMessage({ type: "start", url });  //tell the worker thread that start the operation 
  
      worker.onmessage = async (e) => {           //main thread receive message from worker

        const { type, progress, receivedLength, chunks, error, blob } = e.data;  //receive value from worker e.data destruct it and assing it to variable
  
        if (type === "progress") {
          updateProgress(listItem, progress);  //shows the current progress

          const state = downloadStates.get(url); //get all the values associated with url

          state.receivedLength = receivedLength;  //amount of file is received

          state.chunks = chunks;    //store actually received file

        } else if (type === "complete") {

          updateProgress(listItem, 100);  //dispaly listiem and progress has 100 i.e. complete 

          listItem.querySelector(".status").textContent = "Completed";   //This part of the code sets the text content of the .status element to "Completed".
         
          workers.delete(url);   //delete the entry of worker with specific URL

          worker.terminate();   //terminate the worker
  
          // eownlaod is complete worker is removed Save the file

          const fileHandle = await window.showSaveFilePicker({       //showing window of filepicker to save file
            suggestedName: url.split("/").pop() || "downloaded-file",  //split the url in array of String .pop() extract last element of array
          });                                                          //if url do not have name of file then it will return empty string so replace it with "download-file"
          
          const writable = await fileHandle.createWritable();  //website will interact with the users file system using File System Access API
          await writable.write(blob);     //bolb is any kind of data like, text, image, downloaded file and this line allows user to save file
          await writable.close();         //close the writable

        } else if (type === "error") {
          listItem.querySelector(".status").textContent = error; //take first status class selector and and shows error with style in status class
          workers.delete(url);    //delete the entry of worker with specific URL
          worker.terminate();     //terminate the worker
        }
      };
  
      const pauseButton = listItem.querySelector(".pause-btn"); //selects first css selector .pause-btn

      pauseButton.addEventListener("click", () => {

        const state = downloadStates.get(url);  //get all the values with key url
            
          console.log(state.paused);
          

        if (state.paused) {
            
          worker.postMessage({  //main thread send message to worker
            type: "resume",            //setting parameters
            url,
            receivedLength: state.receivedLength,
            chunks: state.chunks,
          });

          pauseButton.textContent = "Pause";     //showing pause button

          listItem.querySelector(".status").textContent = "Downloading...";
          state.paused = false;
        } else {
          worker.postMessage({ type: "pause" });     //main thread send message to worker
          pauseButton.textContent = "Resume";
          listItem.querySelector(".status").textContent = "Paused";
          state.paused = true;
        }

      });
    });
  });
  
  function createDownloadItem(url) {
    const item = document.createElement("div");   //creating new div to show list of downloads

    item.className = "download-item";      //assign class name download-file to item(div)

     //dynamically set the html content to item
     //innterHTML is used to set the HTML content of the element
    item.innerHTML = `     
      <span class="url">${url}</span>
      <div class="progress-bar"><div class="progress"></div></div>
      <span class="status">Downloading...</span>
      <button class="pause-btn">Pause</button>
    `;
    return item;
  }
  
  function updateProgress(item, progress) {  //takes item, progress and shows progress bar
    const progressBar = item.querySelector(".progress");  //JS that allows you to select first element in DOM that matches with selector
    progressBar.style.width = `${progress}%`;
  }
  