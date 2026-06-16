import { useState } from 'react'

function ModalDistribuicao({ onClose, onSubmit }) {
  const [nome, setNome] = useState('')
  const [versao, setVersao] = useState('')
  const [ano_letivo, setAno_Letivo] = useState('2026/2027')
  const [descricao, setDescricao] = useState('')
  const [erros, setErros] = useState({})

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) onClose()
  }

  const handleSubmit = () => {
    const novosErros = {}
    if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'
    if (!versao.trim()) novosErros.versao = 'A versão é obrigatória.'
    if (!ano_letivo) novosErros.ano_letivo = 'O ano letivo é obrigatório.'

    if (Object.keys(novosErros).length > 0) {
      setErros(novosErros)
      return
    }

    onSubmit({ nome, versao, ano_letivo, descricao })
  }

  return (
    <div className="modal-overlay "  onClick={handleOverlayClick}>
      <div className="modal" role="dialog" aria-modal="true">

        <div className="modal-header">
          <div className="modal-header-left">
            <div className="modal-icon">
              <i className="bi bi-plus modal-btn-confirm_icon"></i>
            </div>
            <div>
              <h2 className="modal-title">Nova Distribuição</h2>
              <p className="modal-subtitle">Preencha os dados para criar uma nova distribuição de serviço.</p>
            </div>
          </div>
          <button className="modal-close" onClick={onClose} title="Fechar">
            <i className="bi bi-x"></i>
          </button>
        </div>

        <div className="modal-body">
          <div className="modal-row two-col">
            <div className="modal-field">
              <label className="modal-label" htmlFor="mNome">Nome <span className="required">*</span></label>
              <div className="modal-input-wrap">
                <input
                  className="modal-input"
                  id="mNome"
                  type="text"
                  placeholder="Ex: Distribuição Inicial"
                  value={nome}
                  onChange={(e) => setNome(e.target.value)}
                />
              </div>
              {erros.nome && <span className="modal-error">{erros.nome}</span>}
            </div>

            <div className="modal-field">
              <label className="modal-label" htmlFor="mVersao">Versão <span className="required">*</span></label>
              <div className="modal-input-wrap">
                <input
                  className="modal-input modal-mono"
                  id="mVersao"
                  type="text"
                  placeholder="Ex: v1.0"
                  value={versao}
                  onChange={(e) => setVersao(e.target.value)}
                />
              </div>
              {erros.versao && <span className="modal-error">{erros.versao}</span>}
            </div>
          </div>

          <div className="modal-field">
            <label className="modal-label" htmlFor="mAno">Ano Letivo <span className="required">*</span></label>
            <div className="modal-input-wrap">
              <select className="modal-input modal-select" id="mAno" value={ano_letivo} onChange={(e) => setAno_Letivo(e.target.value)}>
                <option value="2026/2027">2026/2027</option>
                <option value="2025/2026">2025/2026</option>
                <option value="2024/2025">2024/2025</option>
              </select>
            </div>
            {erros.ano_letivo && <span className="modal-error">{erros.ano_letivo}</span>}
          </div>

          <div className="modal-field">
            <label className="modal-label" htmlFor="mDesc">
              Descrição <span className="modal-label-optional">Opcional</span>
            </label>
            <textarea
              className="modal-textarea"
              id="mDesc"
              rows="3"
              maxLength={300}
              placeholder="Adicione uma breve descrição sobre esta distribuição…"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
            <span className="modal-char-count">{descricao.length} / 300</span>
          </div>
        </div>

        <div className="modal-footer">
          <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
          <button className="modal-btn-confirm" onClick={handleSubmit}>
            <i className="bi bi-plus modal-btn-confirm_icon"></i>
            Criar Distribuição
          </button>
        </div>

      </div>
    </div>
  )
}

export default ModalDistribuicao