import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";


export const AppRoutes = () => {
    return (
        <Routes>
            {/* rota default a ir para o ali para testar etc */}
            <Route path="/" element={<Navigate to="/departamento/1/distribuicoes" replace />} />

            <Route path="/departamento/:id/distribuicoes" element={<DistribuicoesPage />} />


        </Routes>
    );
};