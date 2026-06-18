import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";
import LoginPage from "../pages/LoginPage.jsx";


export const AppRoutes = () => {
    return (
        <Routes>
            {/* rota default a ir para o ali para testar etc */}
            <Route path="/" element={<Navigate to="/auth/login" replace />} />

            <Route path ="/auth/login" element={<LoginPage/>} />

            <Route path="/distribuicoes" element={<DistribuicoesPage />} />


        </Routes>
    );
};