import { useRef } from "react";

export default function UploadForm() {

    const fileRef = useRef();

    // 비동기 방식의 변화
    // XMLHttpRequest 사용 -> JQuery Ajax -> Promise -> fetch -> async/await
    function upload() {
        const formData = new FormData(); // 브라우저가 제공하는 데이터. 자체적으로 이미 multipart/form-data 설정이 포함되어 있음.
                                         // 비동기 방식의 업로드 구현 시 활용도가 높다.
                                         
        const file = fileRef.current.files[0];   // 파일 객체 얻기
        formData.append("file", file)   // 파라미터명을 부여하면서 담아야 한다.
        fetch("http://localhost:8883/api/products/excel", {
            method:"POST",
            body: formData
        })
        .then(response => {
            if(!response.ok) throw new Error("업로드 실패");
            return response.json();
        })
        .then(data => {console.log(data)})
        .catch(err => console.log(err));
    }


    return (
        <div style={{ 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',  
            justifyContent: 'flex-start', 
            minHeight: '100vh',     
            width: '100%',         
            paddingTop: '100px',    
            textAlign: 'center'      
        }}>
            <h3 style={{ marginBottom: '20px' }}>엑셀 상품 등록</h3>
            <div style={{ marginBottom: '15px' }}>
                <input type="file" ref={fileRef}/>
            </div>
            <div>
                <button type="button" style={{ padding: '5px 20px' }} onClick={upload}>업로드</button>
            </div>
        </div>
    );
}