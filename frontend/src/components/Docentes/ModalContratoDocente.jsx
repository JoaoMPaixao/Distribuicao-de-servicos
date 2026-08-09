import { useState } from 'react'

function ModalContratoDocente({ docente, isEdicao, onClose, onSubmit}) {
    const [contratoDataInicio, setDataInicio] = useState(isEdicao ? (docente?.contratoDataInicio || '') : '');
    const [contratoDataFim, setDataFim] = useState(isEdicao ? (docente?.contratoDataFim || '') : '');
    const [contratoDescricao, setDescricao] = useState(isEdicao ? (docente?.contratoDescricao || '') : '');
    const [contratoCategoria, setCategoria] = useState(isEdicao ? (docente?.contratoCategoria || '') : '');
    const [contratoRelacaoJuridica, setRelacaoJuridica] = useState(isEdicao ? (docente?.contratoRelacaoJuridica || '') : '');
    const [contratoDedicacao, setDedicacao] = useState(isEdicao ? (docente?.contratoDedicacao || '') : '');
    const [erros, setErros] = useState({})

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!contratoDataInicio) novosErros.contratoDataInicio = 'A data de inicio é obrigatória.'
        if (!contratoCategoria.trim()) novosErros.contratoCategoria = 'A categoria é obrigatória.'
        if (!contratoRelacaoJuridica.trim()) novosErros.contratoRelacaoJuridica = 'A relacao juridica é obrigatória.'
        if (!contratoDedicacao.trim()) novosErros.contratoDedicacao = 'A dedicacao é obrigatória.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(docente.contratoId, { contratoDescricao, contratoDataInicio, contratoDataFim , contratoCategoria, contratoRelacaoJuridica, contratoDedicacao, docenteId: docente.id })
        } else {
            onSubmit({ contratoDescricao, contratoDataInicio, contratoDataFim , contratoCategoria, contratoRelacaoJuridica, contratoDedicacao, docenteId: docente.id })
        }
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
                            <h2 className="modal-title">
                                {isEdicao ? 'Editar Contrato' : 'Criar Contrato'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o contrato.'
                                    : 'Preencha os dados para criar um novo contrato.'}
                            </p>
                        </div>
                    </div>
                    <button className="modal-close" onClick={onClose} title="Fechar">
                        <i className="bi bi-x"></i>
                    </button>
                </div>

                <div className="modal-body">
                    <div className="modal-row two-col">
                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mDataInicio">Data de Início <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input"
                                    id="mDataInicio"
                                    type="date"
                                    value={contratoDataInicio}
                                    onChange={(e) => setDataInicio(e.target.value)}
                                />
                            </div>
                            {erros.contratoDataInicio && <span className="modal-error">{erros.contratoDataInicio}</span>}
                        </div>

                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mDataFim">Data de Fim <span className="modal-label-optional">Opcional</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input"
                                    id="mDataFim"
                                    type="date"
                                    value={contratoDataFim}
                                    onChange={(e) => setDataFim(e.target.value)}
                                />
                            </div>
                            {erros.contratoDataFim && <span className="modal-error">{erros.contratoDataFim}</span>}
                        </div>
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
                            placeholder="Adicione uma breve descrição sobre este contrato…"
                            value={contratoDescricao}
                            onChange={(e) => setDescricao(e.target.value)}
                        />
                        <span className="modal-char-count">{contratoDescricao.length} / 300</span>
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mCategoria">Categoria <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mCategoria" value={contratoCategoria} onChange={(e) => setCategoria(e.target.value)}>
                                <option value="" disabled hidden>Selecione uma categoria...</option>
                                <option value="PROFESSOR_ADJUNTO">PROFESSOR_ADJUNTO</option>
                                <option value="ASSISTENTE_CONVIDADO">ASSISTENTE_CONVIDADO</option>
                                <option value="PROFESSOR_COORDENADOR">PROFESSOR_COORDENADOR</option>
                                <option value="PROFESSOR_ADJUNTO_CONVIDADO"> PROFESSOR_ADJUNTO_CONVIDADO</option>
                                <option value="ASSISTENTE_2_TRIENIO">ASSISTENTE_2_TRIENIO</option>
                                <option value="EQUIPARADO_ASSISTENTE_2_TRIENIO">EQUIPARADO_ASSISTENTE_2_TRIENIO</option>
                                <option value="EQUIPARADO_PROFESSOR_ADJUNTO">EQUIPARADO_PROFESSOR_ADJUNTO</option>
                                <option value="PROFESSOR_COORDENADOR_CONVIDADO">PROFESSOR_COORDENADOR_CONVIDADO</option>
                                <option value="PROFESSOR_COORDENADOR_AGREGACAO">PROFESSOR_COORDENADOR_AGREGACAO</option>
                            </select>
                        </div>
                        {erros.contratoCategoria && <span className="modal-error">{erros.contratoCategoria}</span>}
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mRelacaoJuridica">Relacao Juridica <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mRelacaoJuridica" value={contratoRelacaoJuridica} onChange={(e) => setRelacaoJuridica(e.target.value)}>
                                <option value="" disabled hidden>Selecione a relacao juridica...</option>
                                <option value="TEMPO_INDETERMINADO">TEMPO_INDETERMINADO</option>
                                <option value="TEMPO_INDETERMINADO_PERIODO_EXPERIMENTAL_5_ANOS">TEMPO_INDETERMINADO_PERIODO_EXPERIMENTAL_5_ANOS</option>
                                <option value="TEMPO_INDETERMINADO_REGIME_TENURE">TEMPO_INDETERMINADO_REGIME_TENURE</option>
                                <option value="TERMO_RESOLUTIVO_CERTO">  TERMO_RESOLUTIVO_CERTO</option>
                            </select>
                        </div>
                        {erros.contratoRelacaoJuridica && <span className="modal-error">{erros.contratoRelacaoJuridica}</span>}
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mDedicacao">Dedicacao <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mDedicacao" value={contratoDedicacao} onChange={(e) => setDedicacao(e.target.value)}>
                                <option value="" disabled hidden>Selecione uma dedicação...</option>
                                <option value="TEMPO_INTEIRO">TEMPO_INTEIRO</option>
                                <option value="TEMPO_PARCIAL">TEMPO_PARCIAL</option>
                                <option value="COMISSAO_SERVICO">COMISSAO_SERVICO</option>
                            </select>
                        </div>
                        {erros.contratoDedicacao && <span className="modal-error">{erros.contratoDedicacao}</span>}
                    </div>


                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar contrato'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalContratoDocente