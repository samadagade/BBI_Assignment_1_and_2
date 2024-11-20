let paused = false;
let receivedLength = 0;
let chunks = [];

self.onmessage = async (e) => {
  const { type, url, receivedLength: resumeLength, chunks: resumeChunks } = e.data;

  if (type === "start" || type === "resume") {
    paused = false;
    receivedLength = resumeLength || 0;
    chunks = resumeChunks || [];

    try {
      const headers = receivedLength                      //ternary operator
        ? { Range: `bytes=${receivedLength}-` }    //for start received length is 0 so header not included and req. fetch whole file
        : {};                                     //if some part of file is downloaded assing header and after resume from that point

      const response = await fetch(url, { headers });  //req to JS's fetch API  fetch() perfrom HTTP req. to specific url header will store 
                                                        //header : gives range, content type

      if (!response.ok && response.status !== 206 && receivedLength > 0) {  //showing error if not get response
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const reader = response.body.getReader();  //body is downloaded data i.e. content of file
                                                //.getReader() allows to read the data

      const contentLength =
        +response.headers.get("Content-Range")?.split("/")[1] || 
        +response.headers.get("Content-Length");       //shows how much bytes received form total size of file

      while (true) {         //reading the file

        if (paused) break;    //check if pause if it is look break and no data is fetch until resume

        const { done, value } = await reader.read();  //done : whether file has fully read //value : chunks of data

        if (done) break;         //if done(file is fully read) break the loop

        chunks.push(value);     //the chunks of data pushed into chunks array

        receivedLength += value.length;  //update received length with adding length of current chunk

        const progress = Math.round((receivedLength / contentLength) * 100);  //to find progress

        postMessage({ type: "progress", progress, receivedLength, chunks });  //send main thread all the variable chunks downloaded so far
      }

      if (!paused) {   //if we not pause download the bolb(dowloaded file) is created using chunks
        const blob = new Blob(chunks);         //creaing bolb(downloaded file) using chunks

        postMessage({ type: "complete", blob });  //send file complete to the main thread
      }

    } catch (error) {  //if the any error occur during fetching an file show that error
      postMessage({ type: "error", error: error.message });
    }
  } else if (type === "pause") {  //stop the download after current chunk is downloaded
    paused = true;
  }
};
