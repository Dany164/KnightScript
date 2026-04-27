import React, { useState } from 'react';
import Header from './components/Header';
import CodeEditor from './components/CodeEditor';
import OutputPanel from './components/OutputPanel';
import './App.css';

const EXAMPLE_CODE = `inicio 
    geo x = 5; 
    geo y = 3; 
    texto mensaje = "Resultado:"; 
    esencia correcto; 
    correcto = luz; 
    si (correcto == luz) entonces { 
        geo suma; 
        suma = x + y; 
        invocar(mensaje); 
        invocar(suma); 
    } 
    mientras (x > 0) { 
        x = x - 1; 
        invocar(x); 
    } 
    retornar x; 
fin
`;

function App() {
  const [code, setCode] = useState(EXAMPLE_CODE);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [netError, setNetError] = useState(null);

  const compile = async () => {
    setLoading(true);
    setNetError(null);
    try {
      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080';
      const res = await fetch(`${apiUrl}/compile`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ code }),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      setResult(data);
    } catch (e) {
      setNetError('No se pudo conectar con el servidor. ¿Está el backend en ejecución?');
    } finally {
      setLoading(false);
    }
  };

  const clear = () => {
    setCode('');
    setResult(null);
    setNetError(null);
  };

  return (
    <div className="app-layout">
      <Header />

      <main className="app-main">
        {/* ── Panel izquierdo: Editor ── */}
        <section className="editor-section">
          <div className="panel-header">
            <span className="panel-icon">⚔</span>
            <h2 className="panel-title">Editor de Código</h2>
          </div>
          <CodeEditor value={code} onChange={setCode} />

          <div className="action-bar">
            <button
              id="btn-compile"
              className={`btn-compile ${loading ? 'btn-loading' : ''}`}
              onClick={compile}
              disabled={loading}
            >
              {loading ? (
                <>
                  <span className="spinner" />
                  Compilando...
                </>
              ) : (
                <>▶ &nbsp;Compilar</>
              )}
            </button>

            <button id="btn-clear" className="btn-clear" onClick={clear} disabled={loading}>
              ✕ &nbsp;Limpiar
            </button>

            {result && (
              <span className={`status-badge ${result.success ? 'status-ok' : 'status-err'}`}>
                {result.success ? '✓ Sin errores' : '✗ Con errores'}
              </span>
            )}
          </div>

          {netError && (
            <div className="net-error">
              <span>⚠</span> {netError}
            </div>
          )}
        </section>

        {/* ── Panel derecho: Resultados ── */}
        <section className="output-section">
          <OutputPanel result={result} />
        </section>
      </main>

      <footer className="app-footer">
        <span>KnightScript © 2026</span>
        <span className="footer-sep">✦</span>
        <span>Lenguaje de programación inspirado en Hollow Knight</span>
      </footer>
    </div>
  );
}

export default App;
