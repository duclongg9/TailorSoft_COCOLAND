import { useState, useEffect } from 'react';

export default function CurrencyInput({ value, onChange, placeholder, className, style, label }) {
  const [displayValue, setDisplayValue] = useState('');

  // Format number to string with dots: 1000000 -> "1.000.000"
  const format = (val) => {
    if (val === null || val === undefined || val === '') return '';
    return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, '.');
  };

  // Convert string with dots back to number: "1.000.000" -> 1000000
  const parse = (str) => {
    return str.replace(/\./g, '');
  };

  useEffect(() => {
    setDisplayValue(format(value));
  }, [value]);

  const handleChange = (e) => {
    const raw = parse(e.target.value);
    // Only allow digits
    if (/^\d*$/.test(raw)) {
      onChange(raw === '' ? '' : Number(raw));
      setDisplayValue(format(raw));
    }
  };

  return (
    <div className="form-group" style={style}>
      {label && <label className="form-label">{label}</label>}
      <div style={{ position: 'relative' }}>
        <input
          type="text"
          className={`form-input ${className || ''}`}
          value={displayValue}
          onChange={handleChange}
          placeholder={placeholder}
          style={{ paddingRight: '40px' }}
        />
        <span style={{
          position: 'absolute',
          right: '16px',
          top: '50%',
          transform: 'translateY(-50%)',
          fontSize: '14px',
          fontWeight: 600,
          color: 'var(--text-muted)',
          pointerEvents: 'none'
        }}>
          ₫
        </span>
      </div>
    </div>
  );
}
