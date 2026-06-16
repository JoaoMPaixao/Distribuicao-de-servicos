import {authInstance} from "./authConfig.jsx";

export const login = async (email, password) => {
    try {
        const { data } = await authInstance.post('/auth/login', { email, password })
        return { success: true, data } // data = dados do utilizador
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const logout = async () => {
    try {
        await authInstance.post('/auth/logout')
        return { success: true }
    } catch (error) {
        return { success: false, status: error.response?.status }
    }
}

export const me = async () => {
    const { data } = await authInstance.get('/auth/me')
    return data
}
