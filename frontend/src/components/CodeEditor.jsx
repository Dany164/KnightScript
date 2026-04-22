import React, { useRef, useState } from 'react';
import './CodeEditor.css';

export default function CodeEditor({ value, onChange }) {
  const textareaRef = useRef(null);
  const lineNumRef  = useRef(null);

  const lines = value.split('\n').length;

  /* Sincroniza el scroll de los números de línea con el textarea */
  const handleScroll = () => {
    if (lineNumRef.current && textareaRef.current) {
      lineNumRef.current.scrollTop = textareaRef.current.scrollTop;
    }
  };

  /* Tab key → insertar 2 espacios en lugar de cambiar foco */
  const handleKeyDown = (e) => {
    if (e.key === 'Tab') {
      e.preventDefault();
      const { selectionStart, selectionEnd } = e.target;
      const newValue =
        value.substring(0, selectionStart) + '  ' + value.substring(selectionEnd);
      onChange(newValue);
      // Reposicionar cursor
      requestAnimationFrame(() => {
        e.target.selectionStart = e.target.selectionEnd = selectionStart + 2;
      });
    }
  };

  return (
    <div className="code-editor">
      {/* Números de línea */}
      <div className="line-numbers" ref={lineNumRef} aria-hidden="true">
        {Array.from({ length: lines }, (_, i) => (
          <div key={i} className="line-num">{i + 1}</div>
        ))}
      </div>

      {/* Textarea principal */}
      <textarea
        id="code-textarea"
        ref={textareaRef}
        className="code-textarea"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onScroll={handleScroll}
        onKeyDown={handleKeyDown}
        spellCheck={false}
        autoComplete="off"
        autoCorrect="off"
        autoCapitalize="off"
        placeholder="// Escribe tu código KnightScript aquí..."
      />
    </div>
  );
}
