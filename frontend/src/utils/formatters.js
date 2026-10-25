
export const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

export const dateFormatter = new Intl.DateTimeFormat('pt-BR')

export function formatCurrency(value) {
  return currencyFormatter.format(value)
}

export function formatDate(value) {
  if (!value) return '—'

  // Evita deslocamento de dia ao formatar uma data ISO.
  const date = new Date(`${value}T12:00:00`)

  if (Number.isNaN(date.getTime())) return '—'

  return dateFormatter.format(date)
}
