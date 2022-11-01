# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* OutputStream 이외의 DataOutputStream 이란 새로운 스트림을 알게됨.
* 한번 index.html 을 요청한 것 뿐이지만 그 안에 있는 여러 CSS 나 js 파일들을 호출하기 위해 서버에 요청을 날림
* 요청 값 첫 줄에 요청 메서드 종류, URL, HTTP 버전등이 명시되어 있음

### 요구사항 2 - get 방식으로 회원가입
쿼리 파라미터가 있는 URL이 들어왔을 때 쿼리 파라미터를 파싱하여 원하는 값에 저장하는 것을 해보았음. 
단순하게 보이는 작업이여도 순차적으로 테스트를 해가면서 단계별로 푸는 것이 중요함.안심하고 다음단계를 진행할 수 있도록 하는 원동력을 제공함.
관심사를 분리하고 싶은 충동이 일어남
HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.




### 요구사항 3 - post 방식으로 회원가입
데이터를 읽기위해서는 content 길이가 필요하다. 그렇지 않으면 반복해서 읽어야한다.
기존 구현 방식 하지만, 스트림 끝에 도달하면 -1을 반환하고 while 문을 빠져나올 것으로 예상했지만 전혀 그렇지 않음
```java
while ((i = br.read()) != -1) {
    char c = (char) i;
    content.append(c);
    log.info("content : {}", content);
}
```

```java

public class IOUtils {
    
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}

```

### 요구사항 4 - redirect 방식으로 이동
HTTP 응답 상태 코드 302 Found 는 URL 리다이렉션을 하게하는 일반적인 방법이다.
뿐만 아니라 특정 URL 을 가리키는 'Location' 헤더가 HTTP 302 응답에 담겨있다.  이건 사용자 에이전트(예, 브라우저)에 대한 초대로, Location 필드에 
지정된 새 URL 에 요청을 하도록 한다. 즉, 새 URL 로 리다리엑션(말 그래도 재 이동, Redirection)이 되는 것이다.

client request
```text
GET /index.html HTTP/1.1
Host: www.example.com
```

server request
```text
HTTP/1.1 302 Found
Location: http://www.iana.org/domains/example/
```

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 