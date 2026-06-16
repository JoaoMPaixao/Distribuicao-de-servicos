import { NavLink } from 'react-router-dom'

const itens = [
    { label: 'Distribuições', icon: 'bi-justify', path: '/distribuicoes' },
    { label: 'Comparações', icon: 'bi-arrow-down-up', path: '/comparacoes' },
    { label: 'Docentes', icon: 'bi-person', path: '/docentes' },
    { label: 'Unidades Curriculares', icon: 'bi-journals', path: '/ucs' },
    { label: 'Cursos', icon: 'bi-mortarboard', path: '/cursos' },
    { label: 'Departamentos', icon: 'bi-house-door', path: '/departamentos' },
    { label: 'Auditoria', icon: 'bi-shield', path: '/auditoria' },
    { label: 'Utilizadores', icon: 'bi-people', path: '/utilizadores' },
    { label: 'Configurações', icon: 'bi-gear', path: '/configuracoes' },
]

function Sidebar() {
    return (
        <aside className="sidebar">
            <div className="sidebar-logo">
                <div className="icon-box">
                    <i className="bi bi-grid sidebar-icon"></i>
                </div>
                <span>Gestão de Serviço Docente</span>
            </div>

            <nav className="sidebar-nav">
                {itens.map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                    >
                        <i className={`bi ${item.icon} nav-icon`}></i>
                        {item.label}
                    </NavLink>
                ))}
            </nav>
        </aside>
    )
}

export default Sidebar