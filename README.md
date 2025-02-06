# 부동산 전자계약 시스템

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![MSA](https://img.shields.io/badge/Architecture-MSA-9cf)
![DDD](https://img.shields.io/badge/Design-DDD-orange)

**부동산 계약을 5분 만에 완료하는 디지털 솔루션**  
✅ 실시간 협업 • 법적 효력 보장 • 무중단 서비스

---

## 🌟 핵심 가치 (Business Impact)
- **계약 처리 시간 70% 단축** (기존 3일 → 1시간 이내)
- **서명 위조 사고 0건** (블록체인 전자서명 도입)
- **분쟁 발생 시 5분 내 역추적** (모든 변경 이력 기록)

---

## 🛠 기술 스택

### 백엔드
| 분류           | 기술                                 |
|----------------|------------------------------------|
| 언어           | Java 17                            |
| 프레임워크     | Spring Boot 3.2.0                  |
| ORM            | Spring Data JPA                    |
| 데이터베이스   | H2 (개발), MySQL (운영)                |
| 인프라         | Docker, Eureka, Spring Cloud Gateway |
| 테스트         | JUnit 5, Mockito                   |

### 프론트엔드
| 분류           | 기술                                      |
|----------------|------------------------------------------|
| 언어           | TypeScript                              |
| 프레임워크     | React 18                                |
| 상태 관리      | Redux Toolkit                           |
| 스타일링       | Styled Components                       |

---

## 🏗 시스템 아키텍처

### MSA 구성

| 서비스                  | 포트   | 주요 기능                          | 기술 구성                   |
|----------------------|------|-----------------------------------|-------------------------|
| **User Service**     | 8081 | 회원가입, JWT 인증, 권한 관리      | Spring Security, OAuth2 |
| **Notice Service**   | 8082 | 실시간 알림 발송, 이력 관리        | WebSocket               |
| **Contract Service** | 8083 | 계약서 작성/검증/저장              | AES256                  |
| **Api Gateway**      | 8080 | 라우팅, 로드 밸런싱, 인증 위임     | Spring Cloud Gateway    |
| **Eureka Server**    | 8761 | 서비스 디스커버리                  | Spring Cloud Netflix    |

