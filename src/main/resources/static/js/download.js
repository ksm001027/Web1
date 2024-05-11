function uploadFile() {
  const formData = new FormData(document.getElementById('uploadForm'));
  fetch('/upload', {
    method: 'POST',
    body: formData,
  })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        const link = document.createElement('a');
        link.href = data.filePath; // 서버에서 반환된 파일 경로
        link.textContent = '다운로드: ' + data.fileName; // 서버에서 반환된 파일 이름
        link.download = data.fileName; // 다운로드 속성 설정
        document.getElementById('downloadLinkContainer').appendChild(link);
      } else {
        alert('파일 업로드 실패');
      }
    })
    .catch(error => console.error('Error uploading file:', error));
}
