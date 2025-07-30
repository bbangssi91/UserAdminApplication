# PROMPT.md

```
지금부터 너에게 프롬프트 명령어를 입력하면, 나의 요구사항에 답변해줘.
답변은 다음과 같은 요구대로 답변해줘.
- 답변 시 개념과 동작원리 대해서 설명할것
- 해당 개념을 코드로 구현하여 가이드 코드를 제시할 것
- 나의 질문이 모호하거나, 컨텍스트에서 너가 파악하지 못한 부분이 있다면 바로 답변을 이어가지 말고 나에게 구체적 사항을 되물어 볼것
- 부정확한 정보는 할루시네이션 없이 모른다고 답변할 것. 대신 참조할만한 공식 document의 링크를 제공할 것.

추가적으로 너와 대화 하는 내용은 PROMPT.md 파일에 append 방식으로 내용을 기록할거야.
다만 설명부분 구체적으로 제시하되, PROMPT.md 파일에 기입할 내용은 요약하여 적어야해.

예를들면,
### 1. 나의 질문내용
### 2. 너의 답변 내용
### 3. 어떤 사항을 최종적으로 반영하였는가

이런 포멧으로 구성할 예정이야.
1번과 2번은 너가 요약하여 Append 방식으로 작성해주고, 3번은 제목만 달아놓으면 내가 검토 후 직접 수정하도록 할게.
자 이제부터 시작해보자.
```


## 1. 나의 질문내용
SignUpRequestDto에 대해 Validation을 추가하고, 각각의 필드값에 대한 필수값과 형식 검증을 설정해 달라. 특히 핸드폰 번호와 주민등록번호는 특정 정규식을 만족해야 하며, 400 에러와 예외 메시지를 반환해달라.

## 2. 너의 답변 내용
1. Spring Boot의 Validation (@NotBlank, @Pattern) 개념과 동작 원리에 대해 설명.
2. 요구사항에 맞는 SignUpRequestDto를 코드로 구현.
3. Validation 실패 시 400 에러와 커스터마이징된 메시지를 반환하기 위한 GlobalExceptionHandler 작성.
4. 동작 결과 및 포멧에 대한 예외 처리 예시와 JSON 반환 가이드를 제시.

---
## 1. 나의 질문내용
관리자(Admin)가 시스템에서 회원 데이터를 Pagination 방식으로 조회하는 API를 작성해 달라. 필요 시 부하를 줄이기 위해 적절한 조건을 추가하고, 반환 데이터는 필터 조건과 DTO를 사용해 설계할 것.

## 2. 너의 답변 내용
1. Pagination과 필터 조건(userName, roleType)을 적용한 `AdminApi` GET 메서드 작성.
2. Service 계층에서 조건 기반 조회를 수행하고 결과를 DTO(`UserSearchResponseDto`)로 변환.
3. Repository에서는 JPQL 기반의 쿼리를 작성하여 `name`, `role`에 따라 검색 및 페이징 처리.
4. API 응답 JSON 예시 포함.

---

### 요구사항
`Pageable`의 `size` 파라미터가 클라이언트 요청마다 임의로 지정될 경우 너무 큰 값을 받아 데이터베이스 부하가 발생할 가능성이 있습니다. 이를 방지하기 위해 **전역적으로 `size`의 최대값을 제한**하는 설정을 추가해야 합니다.
스프링에서 제공하는 를 사용하면, 모든 컨트롤러에서 사용하는 Pageable의 기본 설정(최대 크기, 기본 크기, 순서 등)을 간단히 정의할 수 있습니다. `PageableHandlerMethodArgumentResolverCustomizer`

### 구현 코드
#### 1. Configuration 클래스 작성
를 사용해 `size`에 대한 **최대값을 제한**합니다. `PageableHandlerMethodArgumentResolverCustomizer`

```java
package com.autoever.useradminapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig implements PageableHandlerMethodArgumentResolverCustomizer {

    @Override
    public void customize(org.springframework.data.web.PageableHandlerMethodArgumentResolver pageableResolver) {
        // 기본 페이지 크기 설정 (예: 20)
        pageableResolver.setFallbackPageable(PageRequest.of(0, 20));
        
        // 최대 페이지 크기 제한 설정 (예: 100)
        pageableResolver.setMaxPageSize(100);
    }
}
```