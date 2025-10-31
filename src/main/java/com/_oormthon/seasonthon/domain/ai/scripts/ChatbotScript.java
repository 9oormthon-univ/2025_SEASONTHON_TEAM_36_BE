package com._oormthon.seasonthon.domain.ai.scripts;

import com._oormthon.seasonthon.domain.ai.entity.UserConversation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChatbotScript {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");

        public static String intro() {
                return "안녕! 🐸\n나는 함께 공부계획을 세워주는 개구리 ‘꾸꾸’야!\n" +
                                "너가 목표를 세우고 달성할 때마다 나는 우물 밖 세상을 구경할 수 있어.\n" +
                                "나랑 함께 점프해볼래? 준비됐어?";
        }

        public static String readyResponse(String msg) {
                if (msg.contains("무서") || msg.contains("걱정"))
                        return "그럴 수 있지! 하지만 걱정 마. 내가 함께 도와줄게 🐸\n이름부터 알려줄래?";
                return "좋아! 패기 있는 모습이야 💪\n그 전에 너를 조금 더 알아야 해. 이름을 알려줘!";
        }

        public static String askAge(String name) {
                return "아하! 앞으로 " + name + "이라고 부를게 😄\n그럼 " + name + "은 몇 살이야? (숫자로만 적어줘)";
        }

        public static String ageResponse(int age, String name) {
                String school;
                if (age <= 7)
                        school = "유치원생";
                else if (age <= 13)
                        school = "초등학생";
                else if (age <= 16)
                        school = "중학생";
                else if (age <= 19)
                        school = "고등학생";
                else
                        school = "성인";
                return age + "살이면 " + school + "이겠구나! 👍\n이번에 이루고 싶은 목표가 뭐야? 예를 들어 ‘토익 800점 달성’ 같은 거!";
        }

        public static String askStartDate(String goal) {
                return "좋아! '" + goal + "'를 목표로 해볼게.\n언제부터 시작할까? (yyyy-MM-dd 형식)(예: 2025-11-01)";
        }

        public static String askEndDate(LocalDate start) {
                return "좋아! 시작일은 " + start.format(formatter) + "이네.\n언제까지 끝내고 싶어? (예: 2025-12-31)";
        }

        public static String askStudyDays(LocalDate start, LocalDate end) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

                // 종료일이 시작일보다 빠를 경우 예외처리

                return String.format("좋아, %s ~ %s, 총 %d일 동안이네!\n어떤 요일에 공부할까? (예: 월,수,금)",
                                start.format(formatter), end.format(formatter), days);
        }

        public static String planPrompt(UserConversation convo) {
                return String.format("""
                                당신은 일정 관리 보조 AI입니다.
                                주어진 큰 업무를 실천 가능한 작은 Todo 항목들로 나누세요.
                                사용자 정보를 바탕으로 현실적이고 동기부여가 되는 학습 계획을 제시하세요.

                                [사용자 정보]
                                - 이름: %s
                                - 목표: %s
                                - 기간: %s ~ %s
                                - 공부 요일: %s
                                - 하루 공부 시간: %d분
                                반드시 아래 JSON 스키마를 따르세요.
                                마크다운 코드블록(````json`) 없이 순수 JSON만 반환하세요.

                                description의 내용은 항상 ~하기나 명사형으로 마무리하세요.

                                시작일과 마감일은 항상 정확하게 고려하세요.
                                {
                                    "dDay": "D-3",
                                    "title": "큰 업무 제목",
                                    "endDate": "2025-09-05",
                                    "progressText": "진행 상황 설명",
                                    "progress": 0,
                                    "steps": [
                                      {
                                        "stepDate": "2025-09-02",
                                        "stepOrder": 1,
                                        "description": "세부 작업 설명",
                                        "count": 0,
                                        "isCompleted": false
                                      }
                                    ]
                                }
                                                                """,
                                convo.getUserAge(), convo.getUserName(),
                                convo.getCurrentGoal(),
                                convo.getStartDate().format(formatter),
                                convo.getEndDate().format(formatter),
                                convo.getStudyDays(),
                                convo.getDailyMinutes());

                // [요청]
                // 1. 목표 달성을 위한 주간별 학습 계획을 만들어주세요.
                // 2. 각 주차별로 학습 키워드나 마일스톤을 간단히 제시하세요.
                // 3. 동기부여가 되는 문장으로 마무리해주세요.
                // 4. 답변은 한국어로 간결하고 따뜻하게 작성하세요.
        }
}
