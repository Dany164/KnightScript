import React, { useState } from 'react';
import './OutputPanel.css';

const TABS = [
  { id: 'salida',      label: 'Salida / Consola',   icon: '💻' },
  { id: 'ast',         label: 'Árbol Sintáctico',   icon: '🌿' },
  { id: 'lexicos',     label: 'Errores Léxicos',    icon: '⚠' },
  { id: 'sintacticos', label: 'Errores Sintácticos', icon: '✗' },
  { id: 'simbolos',   label: 'Tabla de Símbolos',  icon: '📜' },
];

export default function OutputPanel({ result }) {
  const [activeTab, setActiveTab] = useState('salida');

  const lexCount = result?.erroresLexicos?.length ?? 0;
  const synCount = result?.erroresSintacticos?.length ?? 0;

  return (
    <div className="output-panel">
      {/* ── Tabs ── */}
      <div className="tab-bar">
        {TABS.map((tab) => {
          const count =
            tab.id === 'lexicos'     ? lexCount :
            tab.id === 'sintacticos' ? synCount  : null;
          return (
            <button
              key={tab.id}
              id={`tab-${tab.id}`}
              className={`tab-btn ${activeTab === tab.id ? 'tab-active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              <span className="tab-icon">{tab.icon}</span>
              <span>{tab.label}</span>
              {count !== null && count > 0 && (
                <span className="tab-badge">{count}</span>
              )}
            </button>
          );
        })}
      </div>

      {/* ── Contenido ── */}
      <div className="tab-content">
        {!result ? (
          <div className="empty-state">
            <div className="empty-icon">⚔</div>
            <p>Compila tu código para ver los resultados aquí.</p>
          </div>
        ) : (
          <>
            {activeTab === 'salida'      && <ConsoleView salida={result.salida} />}
            {activeTab === 'ast'         && <ASTView ast={result.ast} />}
            {activeTab === 'lexicos'     && <ErrorTable errors={result.erroresLexicos} type="Léxico" />}
            {activeTab === 'sintacticos' && <ErrorTable errors={result.erroresSintacticos} type="Sintáctico" />}
            {activeTab === 'simbolos'    && <SymbolTable simbolos={result.tablaSimbolos} />}
          </>
        )}
      </div>
    </div>
  );
}

/* ── Vista de Consola ────────────────────────────────────────── */
function ConsoleView({ salida }) {
  if (salida === undefined || salida === null) {
    return <div className="empty-state"><p>El código no produjo ninguna salida.</p></div>;
  }
  if (salida.trim() === '') {
    return <div className="empty-state ok"><p>Ejecución terminada correctamente sin salidas.</p></div>;
  }
  return (
    <pre className="console-content">{salida}</pre>
  );
}

/* ── Vista del AST ───────────────────────────────────────────── */
function ASTView({ ast }) {
  if (!ast || ast.trim() === '') {
    return <div className="empty-state"><p>No se generó árbol sintáctico.</p></div>;
  }
  return (
    <pre className="ast-content">{ast}</pre>
  );
}

/* ── Tabla de errores ────────────────────────────────────────── */
function ErrorTable({ errors, type }) {
  if (!errors || errors.length === 0) {
    return (
      <div className="empty-state ok">
        <div className="empty-icon ok-icon">✓</div>
        <p>Sin errores {type.toLowerCase()}s.</p>
      </div>
    );
  }
  return (
    <div className="table-wrapper">
      <table className="result-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Línea</th>
            <th>Columna</th>
            <th>Descripción</th>
          </tr>
        </thead>
        <tbody>
          {errors.map((err, i) => (
            <tr key={i} className="error-row">
              <td>{i + 1}</td>
              <td>{err.linea}</td>
              <td>{err.columna}</td>
              <td>{err.descripcion}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

/* ── Tabla de símbolos ───────────────────────────────────────── */
function SymbolTable({ simbolos }) {
  if (!simbolos || simbolos.length === 0) {
    return <div className="empty-state"><p>No se registraron símbolos.</p></div>;
  }
  return (
    <div className="table-wrapper">
      <table className="result-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Identificador</th>
            <th>Tipo</th>
            <th>Valor</th>
            <th>Línea</th>
            <th>Columna</th>
          </tr>
        </thead>
        <tbody>
          {simbolos.map((s, i) => (
            <tr key={i}>
              <td>{i + 1}</td>
              <td className="sym-id">{s.identificador}</td>
              <td className="sym-type">{s.tipo}</td>
              <td className="sym-val">{s.valor}</td>
              <td>{s.linea}</td>
              <td>{s.columna}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
