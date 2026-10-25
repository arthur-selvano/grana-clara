
import { useCallback, useEffect, useState } from 'react'
import { api } from '../services/api'

function getTodayLocal() {
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function createInitialForm() {
  return {
    description: '',
    amount: '',
    date: getTodayLocal(),
    type: 'EXPENSE',
  }
}

export default function useTransactions() {
  const [transactions, setTransactions] = useState([])
  const [form, setForm] = useState(createInitialForm)
  const [editingTransaction, setEditingTransaction] = useState(null)

  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [listError, setListError] = useState('')
  const [formError, setFormError] = useState('')
  const [success, setSuccess] = useState('')
  const [actionError, setActionError] = useState('')
  const [actionSuccess, setActionSuccess] = useState('')

  const [search, setSearch] = useState('')
  const [typeFilter, setTypeFilter] = useState('ALL')
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')

  const loadTransactions = useCallback(async () => {
    try {
      setListError('')
      const data = await api.getTransactions()
      setTransactions(data)
    } catch (err) {
      setListError('Não foi possível carregar as transações.')
      console.error('Erro ao carregar transações:', err)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadTransactions()
  }, [loadTransactions])

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
    setFormError('')
    setSuccess('')
  }

  function handleEdit(transaction) {
    setEditingTransaction(transaction)
    setForm({
      description: transaction.description ?? '',
      amount: String(transaction.amount ?? ''),
      date: transaction.date ?? '',
      type: transaction.type ?? 'EXPENSE',
    })
    setFormError('')
    setSuccess('')
    setActionError('')
    setActionSuccess('')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function handleCancelEdit() {
    setEditingTransaction(null)
    setForm(createInitialForm())
    setFormError('')
    setSuccess('')
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setFormError('')
    setSuccess('')

    const description = form.description.trim()
    const amount = Number(form.amount)

    if (!description) {
      setFormError('Informe a descrição da transação.')
      return
    }

    if (!Number.isFinite(amount) || amount <= 0) {
      setFormError('Informe um valor maior que zero.')
      return
    }

    if (!form.date) {
      setFormError('Informe a data da transação.')
      return
    }

    const transactionData = {
      description,
      amount,
      date: form.date,
      type: form.type,
    }

    setSubmitting(true)

    try {
      if (editingTransaction) {
        await api.updateTransaction(
          editingTransaction.id,
          transactionData,
        )
        setSuccess('Transação atualizada com sucesso!')
        setEditingTransaction(null)
      } else {
        await api.createTransaction(transactionData)
        setSuccess('Transação cadastrada com sucesso!')
      }

      setForm(createInitialForm())
      await loadTransactions()
    } catch (err) {
      setFormError(
        editingTransaction
          ? 'Não foi possível atualizar a transação. Confira os dados e tente novamente.'
          : 'Não foi possível cadastrar a transação. Confira os dados e tente novamente.',
      )
      console.error('Erro ao salvar transação:', err)
    } finally {
      setSubmitting(false)
    }
  }

  async function handleDeleteTransaction(id) {
    setActionError('')
    setActionSuccess('')

    try {
      await api.deleteTransaction(id)

      if (editingTransaction?.id === id) {
        handleCancelEdit()
      }

      setActionSuccess('Transação excluída com sucesso!')
      await loadTransactions()
    } catch (err) {
      setActionError(
        'Não foi possível excluir a transação. Tente novamente.',
      )
      console.error('Erro ao excluir transação:', err)
    }
  }

  const filteredTransactions = transactions.filter((transaction) => {
    const description = (transaction.description ?? '').toLowerCase()
    const matchesSearch = description.includes(search.trim().toLowerCase())
    const matchesType =
      typeFilter === 'ALL' || transaction.type === typeFilter
    const matchesStartDate = !startDate || transaction.date >= startDate
    const matchesEndDate = !endDate || transaction.date <= endDate

    return (
      matchesSearch &&
      matchesType &&
      matchesStartDate &&
      matchesEndDate
    )
  })

  return {
    transactions,
    form,
    loading,
    submitting,
    listError,
    formError,
    success,
    handleChange,
    handleSubmit,
    filteredTransactions,
    search,
    setSearch,
    typeFilter,
    setTypeFilter,
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    handleDeleteTransaction,
    actionError,
    actionSuccess,
    editingTransaction,
    handleEdit,
    handleCancelEdit,
    handleDeleteTransaction,
    actionError,
    actionSuccess,
  }
}
