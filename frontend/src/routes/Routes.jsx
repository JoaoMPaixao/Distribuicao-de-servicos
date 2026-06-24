import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";
import LoginPage from "../pages/LoginPage.jsx";



export const AppRoutes = () => {
    return (
        <Routes>

            <Route path="/" element={<LoginPage/>} />

            <Route path="/distribuicoes" element={<DistribuicoesPage />} />


        </Routes>
    );
};