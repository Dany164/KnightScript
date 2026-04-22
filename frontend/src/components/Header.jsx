import React from 'react';
import './Header.css';

/* SVG ornamental — scrollwork horizontal (estilo Hollow Knight) */
const Ornament = () => (
  <svg className="ornament" viewBox="0 0 400 20" xmlns="http://www.w3.org/2000/svg">
    <line x1="0" y1="10" x2="155" y2="10" stroke="currentColor" strokeWidth="1" />
    <circle cx="160" cy="10" r="2" fill="currentColor" />
    <path d="M170 10 Q180 3 190 10 Q200 17 210 10 Q220 3 230 10" stroke="currentColor" strokeWidth="1" fill="none" />
    <circle cx="240" cy="10" r="2" fill="currentColor" />
    <line x1="245" y1="10" x2="400" y2="10" stroke="currentColor" strokeWidth="1" />
  </svg>
);

export default function Header() {
  return (
    <header className="site-header">
      <div className="header-ornament-top">
        <Ornament />
      </div>

      <div className="header-content">
        <img src="/logo.png" alt="KnightScript Logo" className="header-logo" />
        <div className="header-text">
          <h1 className="header-title">KnightScript</h1>
          <p className="header-subtitle">Compilador Online · Análisis Léxico, Sintáctico & Semántico</p>
        </div>
      </div>

      <div className="header-ornament-bottom">
        <Ornament />
      </div>
    </header>
  );
}
