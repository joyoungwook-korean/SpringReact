import axiosClient from './axiosClient';

export const UserApi = {
    // ✅ Gateway 경로 그대로 사용 (Path=/api/users/**)
    getUsers: () => axiosClient.get('/api/users'),

    // 상세 조회 예시
    getUserById: (userId) =>
        axiosClient.get(`/api/users/${userId}`),

    // 회원 가입 예시
    createUser: (userData) =>
        axiosClient.post('/api/users', userData)
};