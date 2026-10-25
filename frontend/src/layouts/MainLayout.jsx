
import { useEffect, useState } from 'react'
import { NavLink, Outlet } from 'react-router'

function getInitialTheme() {
  try {
    return localStorage.getItem('granaclara-theme') === 'dark'
      ? 'dark'
      : 'light'
  } catch {
    return 'light'
  }
}

export default function MainLayout() {
  const [theme, setTheme] = useState(getInitialTheme)

  useEffect(() => {
    document.documentElement.classList.toggle('dark', theme === 'dark')
    document.documentElement.style.colorScheme = theme

    try {
      localStorage.setItem('granaclara-theme', theme)
    } catch {
      // O tema continua funcionando mesmo sem armazenamento disponível.
    }
  }, [theme])

  function toggleTheme() {
    setTheme((current) => current === 'dark' ? 'light' : 'dark')
  }

  const navClass = ({ isActive }) =>
    `flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition ${
      isActive
        ? 'bg-emerald-600/10 text-emerald-700 dark:bg-emerald-400/10 dark:text-emerald-300'
        : 'text-gray-600 hover:bg-emerald-500/5 hover:text-emerald-700 dark:text-gray-300 dark:hover:bg-white/5 dark:hover:text-emerald-300'
    }`

  return (
    <div className="app-shell min-h-screen lg:flex">
      <aside className="glass-sidebar z-20 flex w-full flex-col border-b border-emerald-900/10 p-4 dark:border-white/10 lg:fixed lg:inset-y-0 lg:w-64 lg:border-b-0 lg:border-r lg:p-6">
        <NavLink to="/" className="flex items-center gap-3 px-2 py-3">
          <span className="flex h-11 w-11 items-center justify-center rounded-2xl bg-emerald-600 text-xl font-bold text-white shadow-lg shadow-emerald-900/15">
            G
          </span>

          <span>
            <span className="block text-lg font-bold tracking-tight text-emerald-950 dark:text-white">
              GranaClara
            </span>
            <span className="block text-xs text-gray-500 dark:text-gray-400">
              Sua vida financeira
            </span>
          </span>
        </NavLink>

        <nav className="mt-7 flex gap-2 lg:flex-col" aria-label="Menu principal">
          <NavLink to="/" end className={navClass}>
            <span aria-hidden="true">⌂</span>
            Visão geral
          </NavLink>

          <NavLink to="/transactions" className={navClass}>
            <span aria-hidden="true">↔</span>
            Transações
          </NavLink>
        </nav>
      </aside>

      <div className="min-w-0 flex-1 lg:ml-64">
        <header className="glass-header sticky top-0 z-10 flex items-center justify-between gap-4 border-b border-emerald-900/10 px-4 py-3 dark:border-white/10 sm:px-8 sm:py-4">
          <div>
            <p className="text-sm font-semibold text-emerald-950 dark:text-white">
              Seu espaço financeiro
            </p>
            <p className="text-xs text-gray-500 dark:text-gray-400">
              Organize hoje, planeje melhor.
            </p>
          </div>

          <button
            type="button"
            onClick={toggleTheme}
            aria-label={
              theme === 'dark'
                ? 'Ativar modo claro'
                : 'Ativar modo escuro'
            }
            aria-pressed={theme === 'dark'}
            title={theme === 'dark' ? 'Modo claro' : 'Modo escuro'}
            className="inline-flex min-h-11 items-center gap-2 rounded-xl border border-emerald-700/15 bg-white/60 px-3 py-2 text-sm font-medium text-emerald-950 shadow-sm backdrop-blur-xl transition hover:bg-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-500 dark:border-white/10 dark:bg-white/5 dark:text-emerald-100 dark:hover:bg-white/10"
          >
            <span className="text-lg" aria-hidden="true">
              {theme === 'dark' ? '☀' : '☾'}
            </span>
            <span className="hidden sm:inline">
              {theme === 'dark' ? 'Modo claro' : 'Modo escuro'}
            </span>
          </button>
        </header>

        <main className="mx-auto w-full max-w-[1600px] p-4 sm:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
