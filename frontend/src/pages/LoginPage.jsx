// pages/LoginPage.jsx
import { useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { login, logout } from '../api/loginApi'
import { UserContext } from '../UserContext'

function LoginPage() {
    const { user } = useContext(UserContext)
    const navigate = useNavigate()

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [mostrarPassword, setMostrarPassword] = useState(false)
    const [erros, setErros] = useState({})
    const [erroGeral, setErroGeral] = useState('')
    const [carregando, setCarregando] = useState(false)

    const validarEmail = (v) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)

    const handleSubmit = async (e) => {
        e.preventDefault()

        // reset de erros
        setErros({})
        setErroGeral('')

        // validação
        const novosErros = {}
        if (!validarEmail(email.trim())) {
            novosErros.email = 'Introduza um endereço de e-mail válido.'
        }
        if (password.trim().length < 1) {
            novosErros.password = 'A palavra-passe é obrigatória.'
        }

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        // chamada à API
        setCarregando(true)
        const response = await login(email, password)
        setCarregando(false)

        if (response.success) {
            navigate('/distribuicoes')
        } else {
            setErroGeral('Credenciais inválidas. Por favor verifique o seu e-mail e palavra-passe.')
        }
    }

    return (
        <div className="right-panel">
            <div className="login-card">

                <div className="card-header">
                    <h1>Bem-vindo de volta</h1>
                    <p>Introduza as suas credenciais para aceder à plataforma.</p>
                </div>

                {/* Erro geral (credenciais inválidas) */}
                {erroGeral && (
                    <div className="alert error">
                        <span>{erroGeral}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} noValidate>

                    {/* Email */}
                    <div className="form-group">
                        <label className="form-label" htmlFor="email">E-mail institucional</label>
                        <div className="input-wrap">
                            <i className="bi bi-envelope input-icon"></i>
                            <input
                                className={`form-input ${erros.email ? 'error' : ''}`}
                                type="email"
                                id="email"
                                placeholder="utilizador@ipportalegre.pt"
                                autoComplete="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        {erros.email && <span className="error-msg show">{erros.email}</span>}
                    </div>

                    {/* Password */}
                    <div className="form-group">
                        <label className="form-label" htmlFor="password">Palavra-passe</label>
                        <div className="input-wrap">
                            <i className="bi bi-lock input-icon"></i>
                            <input
                                className={`form-input ${erros.password ? 'error' : ''}`}
                                type={mostrarPassword ? 'text' : 'password'}
                                id="password"
                                placeholder="••••••••"
                                autoComplete="current-password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                className="toggle-pass"
                                type="button"
                                title="Mostrar/ocultar palavra-passe"
                                onClick={() => setMostrarPassword(!mostrarPassword)}
                            >
                                <i className={`bi ${mostrarPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                            </button>
                        </div>
                        {erros.password && <span className="error-msg show">{erros.password}</span>}
                    </div>

                    {/* Esqueceu password */}
                    <div className="form-row">
                        <a href="#" className="forgot-link">Esqueceu a palavra-passe?</a>
                    </div>

                    {/* Botão submit */}
                    <button className="btn-submit" type="submit" disabled={carregando}>
                        {carregando ? 'A entrar...' : 'Entrar na plataforma'}
                    </button>

                </form>

                <div className="card-footer">
                    Ao entrar, aceita os{' '}
                    <a href="#" style={{ color: 'var(--accent)', textDecoration: 'none' }}>Termos de Utilização</a>
                    {' '}e a{' '}
                    <a href="#" style={{ color: 'var(--accent)', textDecoration: 'none' }}>Política de Privacidade</a>.
                </div>

            </div>
        </div>
    )
}

export default LoginPage