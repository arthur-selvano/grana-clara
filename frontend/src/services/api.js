
const API_URL = 'http://localhost:8080'

async function request(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  })

  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || `Erro HTTP: ${response.status}`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export const api = {
  getTransactions() {
    return request('/transactions')
  },

  getSummary() {
    return request('/transactions/summary')
  },

  createTransaction(transaction) {
    return request('/transactions', {
      method: 'POST',
      body: JSON.stringify(transaction),
    })
  },

  updateTransaction(id, transaction) {
    return request(`/transactions/${id}`, {
      method: 'PUT',
      body: JSON.stringify(transaction),
    })
  },

  deleteTransaction(id) {
    return request(`/transactions/${id}`, {
      method: 'DELETE',
    })
  },
}
