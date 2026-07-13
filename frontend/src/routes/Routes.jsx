import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";
import LoginPage from "../pages/LoginPage.jsx";
import {CursosPage} from "../pages/CursosPage.jsx";



export const AppRoutes = () => {
    return (
        <Routes>

            <Route path="/" element={<LoginPage/>} />

            <Route path="/distribuicoes" element={<DistribuicoesPage />} />

            <Route path="/cursos" element={<CursosPage/>} />


        </Routes>
    );
};