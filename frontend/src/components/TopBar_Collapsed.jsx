import { UserContext } from "../UserContext.jsx";
import { useContext, useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { logout } from "../api/loginApi";

function Topbar({ collapsed, distribuicao }) {
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const [menuAberto, setMenuAberto] = useState(false);
    const menuRef = useRef(null);

    // fecha o menu ao clicar fora dele
    useEffect(() => {
        function handleClickFora(e) {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setMenuAberto(false);
            }
        }
        document.addEventListener('mousedown', handleClickFora);
        return () => document.removeEventListener('mousedown', handleClickFora);
    }, []);

    const handleLogout = async () => {

        navigate('/');
        await logout();
        setUser(null);
        setMenuAberto(false);
        navigate('/');
    };

    return (
        <div className={`topbar ${collapsed ? 'collapsed' : ''}`}>
            <div className="topbar-left">
                <h4>Departamento de {user?.departamento ?? 'unknown'}</h4>
                <h4>/  Distribuição: {distribuicao?.nome ?? 'unknown'}</h4>
            </div>
            <div className="topbar-right">
                <div className="user-chip" ref={menuRef} >
                    <div
                        className="user-chip-trigger"
                        onClick={() => setMenuAberto(!menuAberto)}
                    >
                        <div className="user-info">
                            <div className="name">{user?.nome ?? 'Nome'}</div>
                            <div className="role">{user?.role ?? 'Cargo'}</div>
                        </div>
                        <i className="bi bi-chevron-down chevron-down"></i>
                    </div>

                    {menuAberto && (
                        <div className="user-dropdown">
                            <button className="user-dropdown-item" onClick={handleLogout}>
                                <i className="bi bi-box-arrow-right"></i>
                                Logout
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Topbar;