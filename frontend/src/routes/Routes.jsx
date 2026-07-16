import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";
import LoginPage from "../pages/LoginPage.jsx";
import {CursosPage} from "../pages/CursosPage.jsx";
import {DepartamentosPage} from "../pages/DepartamentosPage.jsx";
import {UtilizadoresPage} from "../pages/UtilizadoresPage.jsx";
import {UcsPage} from "../pages/UcsPage.jsx";


export const AppRoutes = () => {
    return (
        <Routes>

            <Route path="/" element={<LoginPage/>} />

            <Route path="/distribuicoes" element={<DistribuicoesPage />} />

            <Route path="/cursos" element={<CursosPage/>} />

            <Route path="/departamentos" element={<DepartamentosPage/>} />

            <Route path="/utilizadores" element={<UtilizadoresPage/>} />

            <Route path="/ucs" element={<UcsPage/>} />

        </Routes>
    );
};