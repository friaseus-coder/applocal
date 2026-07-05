<!-- Design System -->
<!DOCTYPE html>

<html class="dark" lang="es"><head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<title>Nexus AI | Selecciona tu Compañero</title>
<script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&amp;family=Literata:opsz,wght@7..72,400;600;700&amp;family=JetBrains+Mono&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<!-- Tailwind Configuration -->
<script id="tailwind-config">
      tailwind.config = {
        darkMode: "class",
        theme: {
          extend: {
            "colors": {
                    "on-secondary": "#2d3039",
                    "secondary-container": "#444650",
                    "primary": "#bec6e0",
                    "tertiary": "#dec29a",
                    "on-error-container": "#ffdad6",
                    "secondary-fixed": "#e1e2ed",
                    "error": "#ffb4ab",
                    "on-surface": "#e4e2e4",
                    "on-secondary-fixed-variant": "#444650",
                    "inverse-on-surface": "#303032",
                    "background": "#131315",
                    "on-primary-container": "#798098",
                    "on-primary-fixed": "#131b2e",
                    "on-primary-fixed-variant": "#3f465c",
                    "primary-fixed": "#dae2fd",
                    "outline": "#909097",
                    "on-tertiary-fixed-variant": "#574425",
                    "tertiary-container": "#231500",
                    "on-error": "#690005",
                    "surface-tint": "#bec6e0",
                    "on-secondary-fixed": "#191b24",
                    "on-tertiary-container": "#957d5a",
                    "inverse-surface": "#e4e2e4",
                    "primary-container": "#0f172a",
                    "outline-variant": "#45464d",
                    "on-tertiary": "#3e2d11",
                    "on-tertiary-fixed": "#271901",
                    "secondary-fixed-dim": "#c4c6d1",
                    "surface-container-high": "#2a2a2b",
                    "surface-container": "#1f1f21",
                    "surface-variant": "#353436",
                    "surface-container-lowest": "#0e0e10",
                    "surface-dim": "#131315",
                    "on-background": "#e4e2e4",
                    "inverse-primary": "#565e74",
                    "secondary": "#c4c6d1",
                    "on-primary": "#283044",
                    "surface-bright": "#39393b",
                    "primary-fixed-dim": "#bec6e0",
                    "on-secondary-container": "#b3b4c0",
                    "surface-container-low": "#1b1b1d",
                    "surface": "#131315",
                    "surface-container-highest": "#353436",
                    "on-surface-variant": "#c6c6cd",
                    "tertiary-fixed": "#fcdeb5",
                    "tertiary-fixed-dim": "#dec29a",
                    "error-container": "#93000a"
            },
            "borderRadius": {
                    "DEFAULT": "0.25rem",
                    "lg": "0.5rem",
                    "xl": "0.75rem",
                    "full": "9999px"
            },
            "spacing": {
                    "xxl": "64px",
                    "margin-desktop": "40px",
                    "xl": "32px",
                    "md": "16px",
                    "xs": "4px",
                    "lg": "24px",
                    "margin-mobile": "20px",
                    "base": "8px",
                    "gutter": "24px",
                    "sm": "8px"
            },
            "fontFamily": {
                    "display-lg": ["Literata"],
                    "technical-mono": ["JetBrains Mono"],
                    "display-lg-mobile": ["Literata"],
                    "body-md": ["Inter"],
                    "label-sm": ["Inter"],
                    "heading-md": ["Inter"],
                    "persona-msg": ["Literata"]
            },
            "fontSize": {
                    "display-lg": ["48px", {"lineHeight": "56px", "letterSpacing": "-0.02em", "fontWeight": "600"}],
                    "technical-mono": ["14px", {"lineHeight": "20px", "fontWeight": "400"}],
                    "display-lg-mobile": ["36px", {"lineHeight": "44px", "fontWeight": "600"}],
                    "body-md": ["16px", {"lineHeight": "24px", "fontWeight": "400"}],
                    "label-sm": ["13px", {"lineHeight": "18px", "letterSpacing": "0.05em", "fontWeight": "500"}],
                    "heading-md": ["24px", {"lineHeight": "32px", "fontWeight": "600"}],
                    "persona-msg": ["20px", {"lineHeight": "32px", "fontWeight": "400"}]
            }
          },
        },
      }
    </script>
<style>
        body {
            background-color: #0f172a; /* Deep Privacy Indigo manually set for root experience */
            color: #e4e2e4;
            -webkit-font-smoothing: antialiased;
        }
        .glass-card {
            background: rgba(30, 41, 59, 0.4);
            backdrop-filter: blur(12px);
            border: 1px solid rgba(51, 65, 85, 0.5);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }
        .glass-card:hover {
            background: rgba(30, 41, 59, 0.6);
            border-color: #bec6e0;
            transform: translateY(-4px);
        }
        .glass-card.selected {
            background: rgba(190, 198, 224, 0.1);
            border-color: #bec6e0;
            box-shadow: 0 0 25px rgba(190, 198, 224, 0.15);
        }
        .persona-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 24px;
        }
        .offline-badge {
            background: rgba(20, 184, 166, 0.15);
            color: #14B8A6;
            border: 1px solid rgba(20, 184, 166, 0.3);
        }
        .custom-scrollbar::-webkit-scrollbar { width: 6px; }
        .custom-scrollbar::-webkit-scrollbar-track { background: transparent; }
        .custom-scrollbar::-webkit-scrollbar-thumb { background: #334155; border-radius: 10px; }
    </style>
<style>
    body {
      min-height: max(884px, 100dvh);
    }
  </style>
  </head>
<body class="font-body-md text-body-md overflow-x-hidden custom-scrollbar">
<!-- Top Navigation Shell -->
<nav class="fixed top-0 left-0 w-full z-50 flex justify-between items-center px-margin-mobile md:px-margin-desktop h-16 bg-background/70 backdrop-blur-xl border-b border-outline-variant/30">
<div class="flex items-center gap-3">
<span class="font-display-lg-mobile text-display-lg-mobile text-primary tracking-tight">Nexus AI</span>
</div>
<div class="flex items-center gap-4">
<div class="offline-badge px-3 py-1 rounded-full text-[11px] font-bold tracking-widest flex items-center gap-2">
<span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">verified_user</span>
                100% OFFLINE
            </div>
<span class="material-symbols-outlined text-primary hover:opacity-80 transition-opacity cursor-pointer">security</span>
</div>
</nav>
<!-- Main Content Canvas -->
<main class="pt-32 pb-xxl px-margin-mobile md:px-margin-desktop max-w-[1200px] mx-auto min-h-screen">
<!-- Header Section -->
<header class="mb-xl animate-in fade-in slide-in-from-bottom-4 duration-700">
<h1 class="font-display-lg text-display-lg-mobile md:text-display-lg text-on-surface mb-2">Selecciona tu Compañero</h1>
<p class="text-on-surface-variant font-body-md max-w-xl">
                Elige un arquetipo diseñado para procesar información localmente. Tu privacidad es el núcleo de cada interacción.
            </p>
</header>
<!-- Persona Grid -->
<div class="persona-grid" id="persona-container">
<!-- Cards will be injected here via Script or rendered statically -->
<!-- Persona 1: The Best Friend (Selected) -->
<div class="glass-card selected relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="absolute top-4 right-4">
<span class="material-symbols-outlined text-primary scale-110" style="font-variation-settings: 'FILL' 1;">check_circle</span>
</div>
<div class="w-12 h-12 rounded-lg bg-primary-container flex items-center justify-center mb-md group-hover:shadow-[0_0_20px_rgba(190,198,224,0.15)] transition-shadow">
<span class="material-symbols-outlined text-primary text-3xl">favorite</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Mejor Amigo</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Un oyente empático diseñado para el apoyo emocional y la charla casual diaria.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Psychology</span>
</div>
</div>
<!-- Persona 2: The Mentor -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-tertiary text-3xl">school</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Mentor</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Guía estratégica enfocada en el crecimiento profesional y la resolución de problemas.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Business</span>
</div>
</div>
<!-- Persona 3: The Stoic -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-on-surface-variant text-3xl">shield</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Estoico</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Análisis racional y lógico, libre de sesgos emocionales para decisiones críticas.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Philosophy</span>
</div>
</div>
<!-- Persona 4: The Mystic -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-primary text-3xl">auto_awesome</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Místico</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Explora significados profundos, espiritualidad y conexiones universales.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Bible / Coran</span>
</div>
</div>
<!-- Persona 5: The Architect -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-on-surface-variant text-3xl">architecture</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Arquitecto</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Estructura ideas complejas y diseña sistemas lógicos eficientes.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Engineering</span>
</div>
</div>
<!-- Persona 6: The Creative -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-tertiary text-3xl">palette</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Creativo</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Un colaborador de lluvia de ideas que rompe con lo convencional.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Arts</span>
</div>
</div>
<!-- Persona 7: The Guardian -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-on-surface-variant text-3xl">lock</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Guardián</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Enfocado puramente en la seguridad de datos y protocolos de privacidad.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Sys-Encryption</span>
</div>
</div>
<!-- Persona 8: The Oracle -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-primary text-3xl">visibility</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Oráculo</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Predicciones basadas en datos históricos locales y patrones de usuario.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Data Science</span>
</div>
</div>
<!-- Persona 9: The Sage -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-tertiary text-3xl">self_improvement</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Sabio</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Proporciona sabiduría atemporal y perspectivas de vida equilibradas.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Ancient Texts</span>
</div>
</div>
<!-- Persona 10: The Challenger -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-on-surface-variant text-3xl">swords</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Desafiante</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Cuestiona tus suposiciones para fortalecer tus argumentos y planes.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Debate</span>
</div>
</div>
<!-- Persona 11: The Librarian -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-primary text-3xl">menu_book</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Bibliotecario</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Búsqueda rápida y precisa de información dentro de tus documentos locales.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Documentation</span>
</div>
</div>
<!-- Persona 12: The Comedian -->
<div class="glass-card relative p-lg rounded-xl cursor-pointer group" onclick="selectPersona(this)">
<div class="w-12 h-12 rounded-lg bg-surface-variant flex items-center justify-center mb-md">
<span class="material-symbols-outlined text-tertiary text-3xl">mood</span>
</div>
<h3 class="font-heading-md text-heading-md text-on-surface mb-1">El Comediante</h3>
<p class="text-on-surface-variant text-sm mb-lg leading-relaxed">Aligera el ambiente con humor inteligente y observaciones agudas.</p>
<div class="flex flex-wrap gap-2">
<span class="font-technical-mono text-[10px] uppercase tracking-wider px-2 py-1 rounded bg-secondary-container text-on-secondary-container">Local RAG: Pop Culture</span>
</div>
</div>
</div>
<!-- Call to Action Floating Bar -->
<div class="fixed bottom-24 left-1/2 -translate-x-1/2 w-[calc(100%-40px)] max-w-md z-40">
<button class="w-full bg-primary-container border border-primary text-primary py-4 rounded-xl font-bold flex items-center justify-center gap-2 shadow-2xl hover:bg-primary hover:text-on-primary transition-all active:scale-[0.98]">
<span>Continuar con mi Compañero</span>
<span class="material-symbols-outlined">arrow_forward</span>
</button>
</div>
</main>
<!-- Navigation Shell (Bottom) -->
<nav class="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center px-2 pb-safe h-20 bg-surface-container/80 backdrop-blur-lg border-t border-outline-variant/20 shadow-md rounded-t-xl">
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">chat_bubble</span>
<span class="font-label-sm text-label-sm">Chat</span>
</a>
<a class="flex flex-col items-center justify-center bg-primary-container text-primary rounded-xl px-4 py-1.5 shadow-[0_0_20px_rgba(190,198,224,0.15)] active:scale-90 duration-150" href="#">
<span class="material-symbols-outlined" style="font-variation-settings: 'FILL' 1;">group</span>
<span class="font-label-sm text-label-sm">Personas</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">psychology</span>
<span class="font-label-sm text-label-sm">Memory</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">settings</span>
<span class="font-label-sm text-label-sm">Settings</span>
</a>
</nav>
<script>
        function selectPersona(element) {
            // Remove selection from all others
            document.querySelectorAll('.glass-card').forEach(card => {
                card.classList.remove('selected');
                const check = card.querySelector('.material-symbols-outlined.scale-110');
                if (check) check.parentElement.remove();
                
                // Reset icon background
                const iconContainer = card.querySelector('.w-12');
                iconContainer.classList.remove('bg-primary-container');
                iconContainer.classList.add('bg-surface-variant');
            });

            // Add selection to clicked
            element.classList.add('selected');
            
            // Add checkmark
            const checkWrapper = document.createElement('div');
            checkWrapper.className = 'absolute top-4 right-4';
            checkWrapper.innerHTML = `<span class="material-symbols-outlined text-primary scale-110" style="font-variation-settings: 'FILL' 1;">check_circle</span>`;
            element.appendChild(checkWrapper);

            // Highlight icon container
            const iconContainer = element.querySelector('.w-12');
            iconContainer.classList.remove('bg-surface-variant');
            iconContainer.classList.add('bg-primary-container');
            
            // Micro-interaction feedback
            element.style.transform = 'scale(0.98)';
            setTimeout(() => {
                element.style.transform = '';
            }, 100);
        }

        // Initialize animations on load
        window.addEventListener('DOMContentLoaded', () => {
            const cards = document.querySelectorAll('.glass-card');
            cards.forEach((card, index) => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                setTimeout(() => {
                    card.style.transition = 'all 0.5s cubic-bezier(0.2, 0.8, 0.2, 1)';
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, 100 * index);
            });
        });
    </script>
</body></html>

<!-- Nexus AI - Selección de Persona -->
<!DOCTYPE html>

<html class="dark" lang="es"><head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0, viewport-fit=cover" name="viewport"/>
<title>Nexus AI | El Sabio Estoico</title>
<!-- Fonts -->
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&amp;family=Literata:opsz,wght@7..72,400;600&amp;family=JetBrains+Mono&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
<script id="tailwind-config">
      tailwind.config = {
        darkMode: "class",
        theme: {
          extend: {
            "colors": {
                    "on-secondary": "#2d3039",
                    "secondary-container": "#444650",
                    "primary": "#bec6e0",
                    "tertiary": "#dec29a",
                    "on-error-container": "#ffdad6",
                    "secondary-fixed": "#e1e2ed",
                    "error": "#ffb4ab",
                    "on-surface": "#e4e2e4",
                    "on-secondary-fixed-variant": "#444650",
                    "inverse-on-surface": "#303032",
                    "background": "#131315",
                    "on-primary-container": "#798098",
                    "on-primary-fixed": "#131b2e",
                    "on-primary-fixed-variant": "#3f465c",
                    "primary-fixed": "#dae2fd",
                    "outline": "#909097",
                    "on-tertiary-fixed-variant": "#574425",
                    "tertiary-container": "#231500",
                    "on-error": "#690005",
                    "surface-tint": "#bec6e0",
                    "on-secondary-fixed": "#191b24",
                    "on-tertiary-container": "#957d5a",
                    "inverse-surface": "#e4e2e4",
                    "primary-container": "#0f172a",
                    "outline-variant": "#45464d",
                    "on-tertiary": "#3e2d11",
                    "on-tertiary-fixed": "#271901",
                    "secondary-fixed-dim": "#c4c6d1",
                    "surface-container-high": "#2a2a2b",
                    "surface-container": "#1f1f21",
                    "surface-variant": "#353436",
                    "surface-container-lowest": "#0e0e10",
                    "surface-dim": "#131315",
                    "on-background": "#e4e2e4",
                    "inverse-primary": "#565e74",
                    "secondary": "#c4c6d1",
                    "on-primary": "#283044",
                    "surface-bright": "#39393b",
                    "primary-fixed-dim": "#bec6e0",
                    "on-secondary-container": "#b3b4c0",
                    "surface-container-low": "#1b1b1d",
                    "surface": "#131315",
                    "surface-container-highest": "#353436",
                    "on-surface-variant": "#c6c6cd",
                    "tertiary-fixed": "#fcdeb5",
                    "tertiary-fixed-dim": "#dec29a",
                    "error-container": "#93000a"
            },
            "borderRadius": {
                    "DEFAULT": "0.25rem",
                    "lg": "0.5rem",
                    "xl": "0.75rem",
                    "full": "9999px"
            },
            "spacing": {
                    "xxl": "64px",
                    "margin-desktop": "40px",
                    "xl": "32px",
                    "md": "16px",
                    "xs": "4px",
                    "lg": "24px",
                    "margin-mobile": "20px",
                    "base": "8px",
                    "gutter": "24px",
                    "sm": "8px"
            },
            "fontFamily": {
                    "display-lg": ["Literata"],
                    "technical-mono": ["JetBrains Mono"],
                    "display-lg-mobile": ["Literata"],
                    "body-md": ["Inter"],
                    "label-sm": ["Inter"],
                    "heading-md": ["Inter"],
                    "persona-msg": ["Literata"]
            },
            "fontSize": {
                    "display-lg": ["48px", {"lineHeight": "56px", "letterSpacing": "-0.02em", "fontWeight": "600"}],
                    "technical-mono": ["14px", {"lineHeight": "20px", "fontWeight": "400"}],
                    "display-lg-mobile": ["36px", {"lineHeight": "44px", "fontWeight": "600"}],
                    "body-md": ["16px", {"lineHeight": "24px", "fontWeight": "400"}],
                    "label-sm": ["13px", {"lineHeight": "18px", "letterSpacing": "0.05em", "fontWeight": "500"}],
                    "heading-md": ["24px", {"lineHeight": "32px", "fontWeight": "600"}],
                    "persona-msg": ["20px", {"lineHeight": "32px", "fontWeight": "400"}]
            }
          },
        },
      }
    </script>
<style>
        body {
            background-color: #131315;
            color: #e4e2e4;
            -webkit-font-smoothing: antialiased;
        }
        .glass-panel {
            background: rgba(30, 41, 59, 0.4);
            backdrop-filter: blur(12px);
            border: 1px solid rgba(51, 65, 85, 0.5);
        }
        .ai-bubble {
            border-bottom-left-radius: 4px;
        }
        .user-bubble {
            border-bottom-right-radius: 4px;
        }
        .material-symbols-outlined {
            font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
        }
        .status-pulse {
            animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
        }
        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: .5; }
        }
        ::-webkit-scrollbar {
            width: 6px;
        }
        ::-webkit-scrollbar-track {
            background: transparent;
        }
        ::-webkit-scrollbar-thumb {
            background: #334155;
            border-radius: 10px;
        }
    </style>
<style>
    body {
      min-height: max(884px, 100dvh);
    }
  </style>
  </head>
<body class="font-body-md text-body-md overflow-hidden">
<!-- Header / TopAppBar -->
<header class="fixed top-0 left-0 w-full z-50 flex justify-between items-center px-margin-mobile h-16 bg-background/70 backdrop-blur-xl border-b border-outline-variant/30">
<div class="flex items-center gap-3">
<div class="relative">
<div class="w-10 h-10 rounded-full overflow-hidden border border-primary/20">
<img class="w-full h-full object-cover" data-alt="A highly artistic, minimalist 3D rendering of an abstract marble bust of a Stoic philosopher. The sculpture features smooth, flowing textures and is illuminated by soft, cinematic golden light from the side, creating deep shadows. The background is a dark, moody charcoal gray that emphasizes the white and gray veins of the marble. Modern humanist aesthetic with a premium, museum-like quality." src="https://lh3.googleusercontent.com/aida-public/AB6AXuAooFrM8huu1HBnH1S8JC5efHmgwrOw7reVCydqS3_BOoUxaln5Sebpsfgd_DXlfNCZKf6hta5puR3VO1wyhKtg85VFFVBnJfjtMLRTtbhGm0i5xmsFKzTWDwfxmUwydLxlGtKyHOcfO9cogLDv9L4-9J0cKcOYo5uCuTjiFFEHtwwaXOykHFEbn2vpWdSx8tYlOR4f-5dFVH-Dg2Pm81aSpQZCEgX66uA601XKhRTNBTRZrVj6eqQvKQ"/>
</div>
<div class="absolute bottom-0 right-0 w-3 h-3 bg-emerald-500 border-2 border-background rounded-full status-pulse"></div>
</div>
<div>
<h1 class="font-heading-md text-heading-md text-primary leading-tight">El Sabio Estoico</h1>
<div class="flex items-center gap-1.5">
<span class="font-label-sm text-label-sm text-on-primary-container">Local Engine Active</span>
</div>
</div>
</div>
<div class="flex items-center gap-4">
<div class="hidden md:flex items-center gap-2 px-3 py-1 bg-surface-container rounded-full border border-outline-variant/30">
<span class="material-symbols-outlined text-[18px] text-tertiary">security</span>
<span class="font-label-sm text-label-sm text-on-surface-variant">Soberanía de Datos</span>
</div>
<button class="material-symbols-outlined text-on-surface-variant hover:opacity-80 transition-opacity">search</button>
<button class="material-symbols-outlined text-on-surface-variant hover:opacity-80 transition-opacity">more_vert</button>
</div>
</header>
<!-- Main Chat Canvas -->
<main class="h-screen w-full pt-16 pb-32 overflow-y-auto px-4 md:px-0">
<div class="max-w-[800px] mx-auto py-xxl flex flex-col gap-10">
<!-- Date Separator -->
<div class="flex justify-center">
<span class="px-4 py-1 rounded-full bg-surface-container-low border border-outline-variant/20 text-on-surface-variant font-label-sm text-label-sm">Hoy</span>
</div>
<!-- Message User -->
<div class="flex flex-col items-end gap-2 animate-in fade-in slide-in-from-bottom-4 duration-500">
<div class="max-w-[85%] md:max-w-[70%] bg-primary-container text-on-surface p-4 rounded-2xl user-bubble shadow-sm">
<p class="font-body-md text-body-md leading-relaxed">Me siento muy estresado por el trabajo hoy.</p>
</div>
<span class="font-label-sm text-label-sm text-on-surface-variant/60 mr-2">14:22</span>
</div>
<!-- Message AI -->
<div class="flex flex-col items-start gap-3 animate-in fade-in slide-in-from-bottom-6 duration-700">
<div class="flex items-center gap-3">
<div class="w-8 h-8 rounded-full overflow-hidden border border-primary/20 bg-surface-container">
<img class="w-full h-full object-cover" data-alt="Close up of a classical marble sculpture face, minimalist and clean. Cinematic lighting, soft shadows, premium dark background. Part of a consistent aesthetic of Stoic philosophy and private AI intelligence." src="https://lh3.googleusercontent.com/aida-public/AB6AXuC00vR1riAfMf72cv2FjZK2RIymFTDUxRiBEiWp-EOv6cbOLd1cSlg0MNnLuJit5WLmof9ey8LJmEkLo_KSgq7_2CypquNum0CvIfr7pWPYYd2Au3fYCiUWWyhGSfrUr5PDVSvWYfm9edbqJiQ5F_yCXQ4eyNhmu81pCzka-DbTOIIZqwhvAP7wjS9DdGA7zuTieF7K2YoirfpL0gCf-mYDPGoJm-Bh6ks5L3q8pXjU9ZPqjSocdpKH5A"/>
</div>
<span class="font-label-sm text-label-sm text-primary font-semibold">El Sabio Estoico</span>
</div>
<div class="max-w-[90%] md:max-w-[80%] glass-panel p-6 rounded-3xl ai-bubble shadow-lg">
<p class="font-persona-msg text-persona-msg text-on-surface leading-relaxed italic">
                        "Marco Aurelio nos recordaría que no son los hechos los que nos perturban, sino nuestros juicios sobre ellos. ¿Qué parte de este estrés depende de tu acción directa y qué parte es ruido externo?"
                    </p>
<!-- RAG Indicator -->
<div class="mt-6 flex items-center gap-2 border-t border-outline-variant/20 pt-4">
<span class="material-symbols-outlined text-[16px] text-tertiary">menu_book</span>
<span class="font-label-sm text-label-sm text-on-tertiary-container italic opacity-80">Knowledge Source: Meditaciones de Marco Aurelio</span>
</div>
</div>
<span class="font-label-sm text-label-sm text-on-surface-variant/60 ml-12">14:23 • Local Inferencing</span>
</div>
<!-- More space for scrolling -->
<div class="h-10"></div>
</div>
</main>
<!-- Bottom Input Area -->
<div class="fixed bottom-0 left-0 w-full z-50 px-4 pb-8 pt-4 bg-gradient-to-t from-background via-background to-transparent">
<div class="max-w-[800px] mx-auto">
<div class="glass-panel rounded-2xl p-2 shadow-2xl flex flex-col gap-2 border-outline-variant/40">
<!-- Interaction Bar -->
<div class="flex items-center justify-between px-3 py-1">
<div class="flex items-center gap-4">
<button class="flex items-center gap-2 px-3 py-1.5 rounded-lg hover:bg-surface-variant/50 transition-colors group">
<span class="material-symbols-outlined text-[20px] text-on-surface-variant group-hover:text-primary transition-colors">public</span>
<span class="font-label-sm text-label-sm text-on-surface-variant">Local Web Search</span>
<!-- Toggle (visual only) -->
<div class="w-8 h-4 bg-outline-variant rounded-full relative ml-1">
<div class="absolute left-1 top-0.5 w-3 h-3 bg-on-surface rounded-full"></div>
</div>
</button>
</div>
<div class="flex items-center gap-2">
<span class="font-technical-mono text-technical-mono text-[11px] text-on-surface-variant/50">Llama-3-8B-Private</span>
</div>
</div>
<!-- Input Field -->
<div class="relative flex items-center">
<button class="absolute left-4 material-symbols-outlined text-on-surface-variant hover:text-primary transition-colors">add_circle</button>
<input class="w-full bg-transparent border-0 border-b border-outline-variant/30 focus:ring-0 focus:border-tertiary pl-14 pr-16 py-4 text-on-surface placeholder:text-on-surface-variant/40 font-body-md text-body-md transition-all" placeholder="Escribe tu reflexión..." type="text"/>
<button class="absolute right-3 w-10 h-10 flex items-center justify-center rounded-xl bg-tertiary text-on-tertiary hover:opacity-90 active:scale-95 transition-all shadow-md">
<span class="material-symbols-outlined">send</span>
</button>
</div>
</div>
<!-- Desktop Footer Badge -->
<div class="mt-4 flex justify-center md:hidden">
<div class="flex items-center gap-2 px-3 py-1 bg-surface-container/50 rounded-full border border-outline-variant/20">
<span class="material-symbols-outlined text-[16px] text-tertiary">fingerprint</span>
<span class="font-label-sm text-label-sm text-on-surface-variant/80 uppercase tracking-widest text-[10px]">Soberanía de Datos</span>
</div>
</div>
</div>
</div>
<!-- Bottom Navigation (Mobile) -->
<nav class="md:hidden fixed bottom-0 left-0 w-full z-50 flex justify-around items-center px-2 pb-safe h-20 bg-surface-container/80 backdrop-blur-lg border-t border-outline-variant/20 shadow-md">
<a class="flex flex-col items-center justify-center bg-primary-container text-primary rounded-xl px-4 py-1.5 shadow-[0_0_20px_rgba(190,198,224,0.15)] scale-90" href="#">
<span class="material-symbols-outlined">chat_bubble</span>
<span class="font-label-sm text-label-sm mt-0.5">Chat</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">group</span>
<span class="font-label-sm text-label-sm mt-0.5">Personas</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">psychology</span>
<span class="font-label-sm text-label-sm mt-0.5">Memory</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">settings</span>
<span class="font-label-sm text-label-sm mt-0.5">Settings</span>
</a>
</nav>
<!-- Visual Polish: Atmospheric Background Subtle Glows -->
<div class="fixed top-[-10%] right-[-10%] w-[50%] h-[50%] bg-tertiary/5 blur-[120px] rounded-full pointer-events-none z-0"></div>
<div class="fixed bottom-[-5%] left-[-5%] w-[40%] h-[40%] bg-primary/5 blur-[100px] rounded-full pointer-events-none z-0"></div>
<script>
        // Micro-interactions for the input field
        const input = document.querySelector('input');
        const sendBtn = document.querySelector('button.bg-tertiary');
        
        input.addEventListener('focus', () => {
            input.parentElement.parentElement.classList.add('shadow-[0_0_30px_rgba(222,194,154,0.1)]');
        });
        
        input.addEventListener('blur', () => {
            input.parentElement.parentElement.classList.remove('shadow-[0_0_30px_rgba(222,194,154,0.1)]');
        });

        // Toggle simulation for Web Search
        const searchToggle = document.querySelector('.group');
        let searchActive = false;
        searchToggle.addEventListener('click', () => {
            searchActive = !searchActive;
            const toggleDot = searchToggle.querySelector('.absolute');
            const toggleBg = searchToggle.querySelector('.w-8');
            
            if (searchActive) {
                toggleDot.style.left = '16px';
                toggleDot.style.backgroundColor = '#131315';
                toggleBg.style.backgroundColor = '#bec6e0';
            } else {
                toggleDot.style.left = '4px';
                toggleDot.style.backgroundColor = '#e4e2e4';
                toggleBg.style.backgroundColor = '#45464d';
            }
        });
    </script>
</body></html>

<!-- Nexus AI - Chat (Estoico) -->
<!DOCTYPE html>

<html class="dark" lang="es"><head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0, viewport-fit=cover" name="viewport"/>
<title>Lo que sé de ti - Nexus AI</title>
<script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&amp;family=Literata:ital,wght@0,400;0,600;1,400&amp;family=JetBrains+Mono&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
<style>
        .material-symbols-outlined {
            font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
        }
        .glass-card {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(12px);
            border: 1px solid #334155;
        }
        .active-glow {
            box-shadow: 0 0 20px rgba(190, 198, 224, 0.15);
        }
        body {
            background-color: #0F172A; /* Primary Surface from Style Guidance */
            color: #e4e2e4;
        }
        .safe-teal-pill {
            background-color: #14B8A6;
            color: white;
        }
        /* Custom scrollbar */
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: #334155; border-radius: 10px; }
    </style>
<script id="tailwind-config">
      tailwind.config = {
        darkMode: "class",
        theme: {
          extend: {
            "colors": {
                    "on-secondary": "#2d3039",
                    "secondary-container": "#444650",
                    "primary": "#bec6e0",
                    "tertiary": "#dec29a",
                    "on-error-container": "#ffdad6",
                    "secondary-fixed": "#e1e2ed",
                    "error": "#ffb4ab",
                    "on-surface": "#e4e2e4",
                    "on-secondary-fixed-variant": "#444650",
                    "inverse-on-surface": "#303032",
                    "background": "#131315",
                    "on-primary-container": "#798098",
                    "on-primary-fixed": "#131b2e",
                    "on-primary-fixed-variant": "#3f465c",
                    "primary-fixed": "#dae2fd",
                    "outline": "#909097",
                    "on-tertiary-fixed-variant": "#574425",
                    "tertiary-container": "#231500",
                    "on-error": "#690005",
                    "surface-tint": "#bec6e0",
                    "on-secondary-fixed": "#191b24",
                    "on-tertiary-container": "#957d5a",
                    "inverse-surface": "#e4e2e4",
                    "primary-container": "#0f172a",
                    "outline-variant": "#45464d",
                    "on-tertiary": "#3e2d11",
                    "on-tertiary-fixed": "#271901",
                    "secondary-fixed-dim": "#c4c6d1",
                    "surface-container-high": "#2a2a2b",
                    "surface-container": "#1f1f21",
                    "surface-variant": "#353436",
                    "surface-container-lowest": "#0e0e10",
                    "surface-dim": "#131315",
                    "on-background": "#e4e2e4",
                    "inverse-primary": "#565e74",
                    "secondary": "#c4c6d1",
                    "on-primary": "#283044",
                    "surface-bright": "#39393b",
                    "primary-fixed-dim": "#bec6e0",
                    "on-secondary-container": "#b3b4c0",
                    "surface-container-low": "#1b1b1d",
                    "surface": "#131315",
                    "surface-container-highest": "#353436",
                    "on-surface-variant": "#c6c6cd",
                    "tertiary-fixed": "#fcdeb5",
                    "tertiary-fixed-dim": "#dec29a",
                    "error-container": "#93000a"
            },
            "borderRadius": {
                    "DEFAULT": "0.25rem",
                    "lg": "0.5rem",
                    "xl": "0.75rem",
                    "full": "9999px"
            },
            "spacing": {
                    "xxl": "64px",
                    "margin-desktop": "40px",
                    "xl": "32px",
                    "md": "16px",
                    "xs": "4px",
                    "lg": "24px",
                    "margin-mobile": "20px",
                    "base": "8px",
                    "gutter": "24px",
                    "sm": "8px"
            },
            "fontFamily": {
                    "display-lg": ["Literata"],
                    "technical-mono": ["JetBrains Mono"],
                    "display-lg-mobile": ["Literata"],
                    "body-md": ["Inter"],
                    "label-sm": ["Inter"],
                    "heading-md": ["Inter"],
                    "persona-msg": ["Literata"]
            },
            "fontSize": {
                    "display-lg": ["48px", {"lineHeight": "56px", "letterSpacing": "-0.02em", "fontWeight": "600"}],
                    "technical-mono": ["14px", {"lineHeight": "20px", "fontWeight": "400"}],
                    "display-lg-mobile": ["36px", {"lineHeight": "44px", "fontWeight": "600"}],
                    "body-md": ["16px", {"lineHeight": "24px", "fontWeight": "400"}],
                    "label-sm": ["13px", {"lineHeight": "18px", "letterSpacing": "0.05em", "fontWeight": "500"}],
                    "heading-md": ["24px", {"lineHeight": "32px", "fontWeight": "600"}],
                    "persona-msg": ["20px", {"lineHeight": "32px", "fontWeight": "400"}]
            }
          },
        },
      }
    </script>
<style>
    body {
      min-height: max(884px, 100dvh);
    }
  </style>
  </head>
<body class="font-body-md text-on-surface bg-background selection:bg-primary/30 min-h-screen pb-32">
<!-- TopAppBar -->
<header class="fixed top-0 left-0 w-full z-50 flex justify-between items-center px-margin-mobile h-16 bg-background/70 backdrop-blur-xl border-b border-outline-variant/30">
<div class="flex items-center gap-3">
<div class="w-8 h-8 rounded-full bg-primary-container flex items-center justify-center overflow-hidden border border-outline-variant/50">
<img class="w-full h-full object-cover" data-alt="A sophisticated digital portrait of a high-tech AI persona avatar, with soft neural network patterns glowing in a deep blue and gold aesthetic. The lighting is ethereal and professional, symbolizing a secure and private companion. The style is modern humanist with glassmorphism textures." src="https://lh3.googleusercontent.com/aida-public/AB6AXuDONUZuVlFC0dw1J5VobR7Cnp31cy8mH1oXhv91omtKJ3oS9jLj5h1YHoqp74wvOGgaATLmKZbjB6Z48QYYNLOrF1dWTEHtQ6Ni4flJEDxheaztGdVXjEUVHo_JcWlRTq-kTmJy5Pl-urDG7gkLqC7i92PoWYlAskfuN8wqIZ1BxD45KFkYFZyTdlvbfQJIVexLXnPw34Aa0R_UmH50L9HxZNfBX-XlbT4kFazSA3wedZHFtxhCD5M1ZQ"/>
</div>
<span class="font-display-lg-mobile text-display-lg-mobile text-primary tracking-tight">Nexus AI</span>
</div>
<div class="flex items-center gap-4">
<div class="hidden md:flex gap-2">
<span class="safe-teal-pill px-3 py-1 rounded-full font-label-sm text-label-sm flex items-center gap-1.5">
<span class="material-symbols-outlined text-[14px]" style="font-variation-settings: 'FILL' 1;">verified_user</span>
                    Offline Engine
                </span>
</div>
<button class="text-primary hover:opacity-80 transition-opacity active:scale-95 duration-200">
<span class="material-symbols-outlined">security</span>
</button>
</div>
</header>
<!-- Main Content Canvas -->
<main class="pt-24 px-margin-mobile max-w-7xl mx-auto w-full">
<!-- Header Section -->
<section class="mb-xl">
<h1 class="font-heading-md text-heading-md text-on-surface mb-xs">Memoria Local del Nexo</h1>
<p class="text-on-surface-variant font-body-md text-body-md max-w-2xl">
                Estos datos nunca salen de tu teléfono. Lo que Nexus aprende de ti se guarda en una tabla privada (GustoUsuario) para personalizar tu experiencia local. Puedes borrarlos en cualquier momento.
            </p>
</section>
<!-- Bento Grid for Categories -->
<div class="grid grid-cols-1 md:grid-cols-3 gap-gutter">
<!-- Section: Mis Gustos -->
<div class="space-y-md">
<div class="flex items-center gap-2 px-1">
<span class="material-symbols-outlined text-primary">favorite</span>
<h2 class="font-heading-md text-label-sm uppercase tracking-wider text-primary">Mis Gustos</h2>
</div>
<div class="space-y-sm" id="gustos-container">
<!-- Item Card -->
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Café espresso</span>
<span class="text-tertiary-fixed font-label-sm text-[10px] mt-1 border border-tertiary-fixed/30 rounded px-1.5 w-fit">GUSTA</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
<!-- Item Card -->
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Jazz clásico</span>
<span class="text-tertiary-fixed font-label-sm text-[10px] mt-1 border border-tertiary-fixed/30 rounded px-1.5 w-fit">GUSTA</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
</div>
</div>
<!-- Section: Valores Morales -->
<div class="space-y-md">
<div class="flex items-center gap-2 px-1">
<span class="material-symbols-outlined text-primary">balance</span>
<h2 class="font-heading-md text-label-sm uppercase tracking-wider text-primary">Valores Morales</h2>
</div>
<div class="space-y-sm" id="valores-container">
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Transparencia</span>
<span class="text-tertiary-fixed font-label-sm text-[10px] mt-1 border border-tertiary-fixed/30 rounded px-1.5 w-fit">GUSTA</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Impuntualidad</span>
<span class="text-error font-label-sm text-[10px] mt-1 border border-error/30 rounded px-1.5 w-fit uppercase">No Gusta</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
</div>
</div>
<!-- Section: Estresores -->
<div class="space-y-md">
<div class="flex items-center gap-2 px-1">
<span class="material-symbols-outlined text-primary">bolt</span>
<h2 class="font-heading-md text-label-sm uppercase tracking-wider text-primary">Estresores</h2>
</div>
<div class="space-y-sm" id="estresores-container">
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Despertarse temprano</span>
<span class="text-error font-label-sm text-[10px] mt-1 border border-error/30 rounded px-1.5 w-fit uppercase">No Gusta</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
<div class="glass-card p-md rounded-xl flex items-center justify-between group transition-all hover:active-glow">
<div class="flex flex-col">
<span class="font-body-md text-on-surface">Ruidos estridentes</span>
<span class="text-error font-label-sm text-[10px] mt-1 border border-error/30 rounded px-1.5 w-fit uppercase">No Gusta</span>
</div>
<button class="text-on-surface-variant hover:text-error transition-colors p-2" onclick="this.parentElement.remove()">
<span class="material-symbols-outlined">delete</span>
</button>
</div>
</div>
</div>
</div>
<!-- Action Footer -->
<div class="mt-xxl pt-xl border-t border-outline-variant/20 flex flex-col items-center">
<button class="flex items-center gap-2 border border-error text-error px-lg py-md rounded-xl font-heading-md text-body-md hover:bg-error/10 transition-all active:scale-95 duration-150" onclick="confirmClear()">
<span class="material-symbols-outlined">delete_forever</span>
                Limpiar Memoria Completa
            </button>
<p class="mt-md text-on-surface-variant font-label-sm text-label-sm opacity-60">Esta acción es irreversible y reiniciará el perfil de aprendizaje.</p>
</div>
</main>
<!-- Bottom Navigation Shell -->
<nav class="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center px-2 pb-safe h-20 bg-surface-container/80 backdrop-blur-lg border-t border-outline-variant/20 shadow-md rounded-t-xl">
<button class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors">
<span class="material-symbols-outlined">chat_bubble</span>
<span class="font-label-sm text-label-sm">Chat</span>
</button>
<button class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors">
<span class="material-symbols-outlined">group</span>
<span class="font-label-sm text-label-sm">Personas</span>
</button>
<button class="flex flex-col items-center justify-center bg-primary-container text-primary rounded-xl px-4 py-1.5 shadow-[0_0_20px_rgba(190,198,224,0.15)] active:scale-90 duration-150">
<span class="material-symbols-outlined" style="font-variation-settings: 'FILL' 1;">psychology</span>
<span class="font-label-sm text-label-sm">Memory</span>
</button>
<button class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors">
<span class="material-symbols-outlined">settings</span>
<span class="font-label-sm text-label-sm">Settings</span>
</button>
</nav>
<script>
        function confirmClear() {
            if (confirm("¿Estás seguro de que deseas borrar toda tu memoria local? Nexus olvidará tus preferencias por completo.")) {
                document.getElementById('gustos-container').innerHTML = '';
                document.getElementById('valores-container').innerHTML = '';
                document.getElementById('estresores-container').innerHTML = '';
                alert("Memoria local purgada con éxito.");
            }
        }

        // Micro-interaction: Glow on hover for cards
        document.querySelectorAll('.glass-card').forEach(card => {
            card.addEventListener('mouseenter', () => {
                card.style.transform = 'translateY(-2px)';
            });
            card.addEventListener('mouseleave', () => {
                card.style.transform = 'translateY(0px)';
            });
        });
    </script>
</body></html>

<!-- Nexus AI - Mi Memoria Local -->
<!DOCTYPE html>

<html class="dark" lang="es"><head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<title>Nexus AI - Avatar de Legado</title>
<script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Literata:opsz,wght@7..72,400;600;700&family=JetBrains+Mono:wght@400&family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Literata:wght@100..900&display=swap" rel="stylesheet"/>
<script id="tailwind-config">
      tailwind.config = {
        darkMode: "class",
        theme: {
          extend: {
            "colors": {
                    "on-secondary": "#2d3039",
                    "secondary-container": "#444650",
                    "primary": "#bec6e0",
                    "tertiary": "#dec29a",
                    "on-error-container": "#ffdad6",
                    "secondary-fixed": "#e1e2ed",
                    "error": "#ffb4ab",
                    "on-surface": "#e4e2e4",
                    "on-secondary-fixed-variant": "#444650",
                    "inverse-on-surface": "#303032",
                    "background": "#131315",
                    "on-primary-container": "#798098",
                    "on-primary-fixed": "#131b2e",
                    "on-primary-fixed-variant": "#3f465c",
                    "primary-fixed": "#dae2fd",
                    "outline": "#909097",
                    "on-tertiary-fixed-variant": "#574425",
                    "tertiary-container": "#231500",
                    "on-error": "#690005",
                    "surface-tint": "#bec6e0",
                    "on-secondary-fixed": "#191b24",
                    "on-tertiary-container": "#957d5a",
                    "inverse-surface": "#e4e2e4",
                    "primary-container": "#0f172a",
                    "outline-variant": "#45464d",
                    "on-tertiary": "#3e2d11",
                    "on-tertiary-fixed": "#271901",
                    "secondary-fixed-dim": "#c4c6d1",
                    "surface-container-high": "#2a2a2b",
                    "surface-container": "#1f1f21",
                    "surface-variant": "#353436",
                    "surface-container-lowest": "#0e0e10",
                    "surface-dim": "#131315",
                    "on-background": "#e4e2e4",
                    "inverse-primary": "#565e74",
                    "secondary": "#c4c6d1",
                    "on-primary": "#283044",
                    "surface-bright": "#39393b",
                    "primary-fixed-dim": "#bec6e0",
                    "on-secondary-container": "#b3b4c0",
                    "surface-container-low": "#1b1b1d",
                    "surface": "#131315",
                    "surface-container-highest": "#353436",
                    "on-surface-variant": "#c6c6cd",
                    "tertiary-fixed": "#fcdeb5",
                    "tertiary-fixed-dim": "#dec29a",
                    "error-container": "#93000a"
            },
            "borderRadius": {
                    "DEFAULT": "0.25rem",
                    "lg": "0.5rem",
                    "xl": "0.75rem",
                    "full": "9999px"
            },
            "spacing": {
                    "xxl": "64px",
                    "margin-desktop": "40px",
                    "xl": "32px",
                    "md": "16px",
                    "xs": "4px",
                    "lg": "24px",
                    "margin-mobile": "20px",
                    "base": "8px",
                    "gutter": "24px",
                    "sm": "8px"
            },
            "fontFamily": {
                    "display-lg": ["Literata"],
                    "technical-mono": ["JetBrains Mono"],
                    "display-lg-mobile": ["Literata"],
                    "body-md": ["Inter"],
                    "label-sm": ["Inter"],
                    "heading-md": ["Inter"],
                    "persona-msg": ["Literata"]
            },
            "fontSize": {
                    "display-lg": ["48px", {"lineHeight": "56px", "letterSpacing": "-0.02em", "fontWeight": "600"}],
                    "technical-mono": ["14px", {"lineHeight": "20px", "fontWeight": "400"}],
                    "display-lg-mobile": ["36px", {"lineHeight": "44px", "fontWeight": "600"}],
                    "body-md": ["16px", {"lineHeight": "24px", "fontWeight": "400"}],
                    "label-sm": ["13px", {"lineHeight": "18px", "letterSpacing": "0.05em", "fontWeight": "500"}],
                    "heading-md": ["24px", {"lineHeight": "32px", "fontWeight": "600"}],
                    "persona-msg": ["20px", {"lineHeight": "32px", "fontWeight": "400"}]
            }
          },
        },
      }
    </script>
<style>
      .material-symbols-outlined {
        font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
      }
      body {
        background-color: #0F172A; /* Using Primary Surface from Guidance */
        color: #e4e2e4;
      }
      .glass-card {
        background: rgba(30, 41, 59, 0.7);
        backdrop-filter: blur(12px);
        border: 1px solid #334155;
      }
      .neural-glow {
        box-shadow: 0 0 20px rgba(190, 198, 224, 0.15);
      }
      .progress-ring-circle {
        transition: stroke-dashoffset 0.35s;
        transform: rotate(-90deg);
        transform-origin: 50% 50%;
      }
      /* Custom Sliders */
      input[type=range] {
        -webkit-appearance: none;
        background: transparent;
      }
      input[type=range]::-webkit-slider-runnable-track {
        height: 2px;
        background: #334155;
      }
      input[type=range]::-webkit-slider-thumb {
        -webkit-appearance: none;
        height: 16px;
        width: 16px;
        border-radius: 50%;
        background: #bec6e0;
        margin-top: -7px;
        cursor: pointer;
        box-shadow: 0 0 10px rgba(190, 198, 224, 0.3);
      }
    </style>
<style>
    body {
      min-height: max(884px, 100dvh);
    }
  </style>
  </head>
<body class="font-body-md text-body-md overflow-x-hidden">
<!-- Top Navigation Anchor -->
<header class="fixed top-0 left-0 w-full z-50 flex justify-between items-center px-margin-mobile md:px-margin-desktop h-16 bg-background/70 backdrop-blur-xl border-b border-outline-variant/30">
<div class="flex items-center gap-3">
<span class="material-symbols-outlined text-primary text-2xl">auto_awesome</span>
<h1 class="font-display-lg-mobile text-display-lg-mobile md:font-display-lg md:text-display-lg text-primary tracking-tight">Nexus AI</h1>
</div>
<div class="flex items-center gap-4">
<div class="hidden md:flex bg-on-tertiary-container/20 px-3 py-1 rounded-full items-center gap-2">
<span class="w-2 h-2 bg-tertiary rounded-full animate-pulse"></span>
<span class="font-label-sm text-label-sm text-tertiary">Offline Engine Active</span>
</div>
<span class="material-symbols-outlined text-primary cursor-pointer hover:opacity-80 transition-opacity">security</span>
</div>
</header>
<main class="pt-24 pb-32 px-margin-mobile md:px-xxl max-w-[1200px] mx-auto min-h-screen">
<!-- Header Section -->
<section class="mb-xl text-center md:text-left">
<h2 class="font-display-lg text-display-lg text-on-surface mb-2">Avatar de Legado</h2>
<p class="text-on-surface-variant font-body-md max-w-2xl">Immortaliza la esencia de tus seres queridos o figuras históricas en un entorno privado y seguro.</p>
</section>
<div class="grid grid-cols-1 lg:grid-cols-12 gap-gutter">
<!-- Left Column: Settings -->
<div class="lg:col-span-7 space-y-gutter">
<!-- Identification Card -->
<div class="glass-card p-lg rounded-xl neural-glow">
<h3 class="font-heading-md text-heading-md text-primary mb-md flex items-center gap-2">
<span class="material-symbols-outlined">person_add</span>
                        Identidad
                    </h3>
<div class="space-y-lg">
<div class="relative">
<label class="block font-label-sm text-label-sm text-on-surface-variant mb-xs">Nombre del Avatar</label>
<input class="w-full bg-transparent border-b border-outline-variant focus:border-primary focus:ring-0 text-on-surface font-persona-msg py-2 transition-colors outline-none" placeholder="Ingresa un nombre..." type="text" value="Alberto M. Valdés"/>
</div>
<div class="relative">
<label class="block font-label-sm text-label-sm text-on-surface-variant mb-xs">Relación/Contexto</label>
<input class="w-full bg-transparent border-b border-outline-variant focus:border-primary focus:ring-0 text-on-surface font-body-md py-2 transition-colors outline-none" placeholder="Ej: Historiador, Mentor, Familiar..." type="text" value="Abuelo Paterno"/>
</div>
</div>
</div>
<!-- Memory Upload Card -->
<div class="glass-card p-lg rounded-xl">
<h3 class="font-heading-md text-heading-md text-primary mb-md flex items-center gap-2">
<span class="material-symbols-outlined">description</span>
                        Cargar Documentos (.txt, .pdf)
                    </h3>
<div class="border-2 border-dashed border-outline-variant/50 rounded-lg p-lg text-center hover:border-primary/50 transition-colors cursor-pointer mb-md">
<span class="material-symbols-outlined text-4xl text-on-surface-variant mb-2">cloud_upload</span>
<p class="font-label-sm text-label-sm text-on-surface-variant">Arrastra archivos aquí o haz clic para buscar</p>
</div>
<div class="space-y-sm">
<div class="flex items-center justify-between bg-surface-container/50 p-sm rounded-lg border border-outline-variant/30">
<div class="flex items-center gap-3">
<span class="material-symbols-outlined text-tertiary">article</span>
<span class="font-label-sm text-label-sm text-on-surface">diario_abuelo.txt</span>
</div>
<span class="material-symbols-outlined text-error cursor-pointer text-sm">close</span>
</div>
</div>
</div>
<!-- Personality Section -->
<div class="glass-card p-lg rounded-xl">
<h3 class="font-heading-md text-heading-md text-primary mb-lg flex items-center gap-2">
<span class="material-symbols-outlined">psychology</span>
                        Configuración de Personalidad
                    </h3>
<div class="space-y-xl">
<div class="space-y-md">
<div class="flex justify-between items-center">
<span class="font-label-sm text-label-sm text-on-surface-variant">Temperamento</span>
<span class="font-technical-mono text-technical-mono text-primary">Neutral</span>
</div>
<div class="flex items-center gap-4">
<span class="font-label-sm text-label-sm text-on-surface-variant w-16">Formal</span>
<input class="flex-1" max="100" min="0" type="range" value="50"/>
<span class="font-label-sm text-label-sm text-on-surface-variant w-16 text-right">Casual</span>
</div>
</div>
<div class="space-y-md">
<div class="flex justify-between items-center">
<span class="font-label-sm text-label-sm text-on-surface-variant">Profundidad</span>
<span class="font-technical-mono text-technical-mono text-tertiary">Analítico ++</span>
</div>
<div class="flex items-center gap-4">
<span class="font-label-sm text-label-sm text-on-surface-variant w-16">Emocional</span>
<input class="flex-1" max="100" min="0" type="range" value="75"/>
<span class="font-label-sm text-label-sm text-on-surface-variant w-16 text-right">Analítico</span>
</div>
</div>
</div>
</div>
</div>
<!-- Right Column: Status & Preview -->
<div class="lg:col-span-5 space-y-gutter">
<!-- Progress Card -->
<div class="glass-card p-lg rounded-xl flex flex-col items-center text-center">
<h3 class="font-heading-md text-heading-md text-on-surface mb-lg">Procesando Memoria...</h3>
<div class="relative w-48 h-48 flex items-center justify-center mb-md">
<svg class="w-full h-full" viewbox="0 0 100 100">
<circle class="text-surface-variant" cx="50" cy="50" fill="transparent" r="40" stroke="currentColor" stroke-width="6"></circle>
<circle class="text-primary progress-ring-circle" cx="50" cy="50" fill="transparent" r="40" stroke="currentColor" stroke-dasharray="251.2" stroke-dashoffset="55.2" stroke-linecap="round" stroke-width="6"></circle>
</svg>
<div class="absolute inset-0 flex flex-col items-center justify-center">
<span class="font-display-lg-mobile text-display-lg-mobile text-primary">78%</span>
<span class="font-label-sm text-label-sm text-on-surface-variant">Local RAG</span>
</div>
</div>
<p class="font-technical-mono text-technical-mono text-on-tertiary-container bg-tertiary-container/30 px-3 py-1 rounded-full">
                        index_v4_vector_embedding.bin
                    </p>
</div>
<!-- Preview Image/Archetype -->
<div class="rounded-xl overflow-hidden relative group aspect-square glass-card">
<div class="w-full h-full bg-cover bg-center transition-transform duration-700 group-hover:scale-105 opacity-60" data-alt="A cinematic, warm-toned portrait of a wise elderly man with gentle eyes and grey hair, sitting in a library filled with old leather-bound books. The lighting is soft and golden, resembling a nostalgic memory. The overall aesthetic is intimate, respectful, and high-fidelity, using the deep primary and neural gold color palette of the Nexus AI brand." style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuClrdRaLyzvATMppVCbrd_56QUGjvB4Zj6rC1-nOHA8Wo82Zj9g93t9Oum7qRL0y--FA2cViz9msP9QxbqsKVh5C4qFtBz7wLQtmDCDgZfGPdRExeJGlSwhTr_O0p9Bd5Nq9bUSPArEVKSRJbK006v_IJ37kEHr2Jwak1i76ME80pbcLRXMjazth7FeVLM0ZiS5601E4rUUCd-V0OQWisYqnZKjqm0kyxafZMi21-9El0tYDphtOYt7Dw')"></div>
<div class="absolute inset-0 bg-gradient-to-t from-background via-transparent to-transparent flex items-end p-lg">
<div>
<span class="font-label-sm text-label-sm text-primary uppercase tracking-widest mb-1 block">Arquetipo Activo</span>
<h4 class="font-display-lg-mobile text-display-lg-mobile text-on-surface">El Sabio</h4>
</div>
</div>
</div>
</div>
</div>
<!-- Sticky Footer Action -->
<div class="fixed bottom-0 left-0 w-full h-24 bg-surface-container/80 backdrop-blur-lg border-t border-outline-variant/20 px-margin-mobile flex items-center justify-between z-40">
<div class="hidden md:flex flex-col">
<span class="font-label-sm text-label-sm text-on-surface-variant">Estado de Cifrado</span>
<span class="font-body-md text-body-md text-primary flex items-center gap-1">
<span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">lock</span>
                    End-to-End Local
                </span>
</div>
<button class="w-full md:w-auto bg-primary-container border border-primary text-primary px-xxl py-md rounded-xl font-heading-md text-heading-md hover:shadow-[0_0_20px_rgba(190,198,224,0.3)] active:scale-95 transition-all duration-200">
                Activar Legado
            </button>
</div>
</main>
<!-- Bottom Navigation (Shared Component Placeholder Logic) -->
<nav class="fixed md:hidden bottom-0 left-0 w-full z-50 flex justify-around items-center px-2 pb-safe h-20 bg-surface-container/80 backdrop-blur-lg border-t border-outline-variant/20 shadow-md">
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">chat_bubble</span>
<span class="font-label-sm text-label-sm">Chat</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">group</span>
<span class="font-label-sm text-label-sm">Personas</span>
</a>
<a class="flex flex-col items-center justify-center bg-primary-container text-primary rounded-xl px-4 py-1.5 shadow-[0_0_20px_rgba(190,198,224,0.15)]" href="#">
<span class="material-symbols-outlined" style="font-variation-settings: 'FILL' 1;">psychology</span>
<span class="font-label-sm text-label-sm">Memory</span>
</a>
<a class="flex flex-col items-center justify-center text-on-surface-variant px-4 py-1.5 hover:text-primary transition-colors" href="#">
<span class="material-symbols-outlined">settings</span>
<span class="font-label-sm text-label-sm">Settings</span>
</a>
</nav>
<script>
        // Micro-interactions for Sliders
        const sliders = document.querySelectorAll('input[type=range]');
        sliders.forEach(slider => {
            slider.addEventListener('input', (e) => {
                const value = e.target.value;
                // Subtle feedback or animation can be added here
            });
        });

        // Simulating some loading logic for the circular progress
        let progress = 78;
        const circle = document.querySelector('.progress-ring-circle');
        const radius = circle.r.baseVal.value;
        const circumference = radius * 2 * Math.PI;

        circle.style.strokeDasharray = `${circumference} ${circumference}`;
        
        function setProgress(percent) {
            const offset = circumference - (percent / 100 * circumference);
            circle.style.strokeDashoffset = offset;
        }

        setProgress(progress);
    </script>
</body></html>

<!-- Nexus AI - Crear Legado -->
# Project Brief: Nexus AI - The Sovereign Companion

## Executive Summary
Nexus AI is a paradigm-shifting mobile assistant for Android that prioritizes **Absolute Privacy** and **Zero-Latency Intelligence**. Unlike cloud-based assistants, Nexus operates 100% offline, executing its Large Language Model (LLM) and Vector Database (RAG) directly on the user's smartphone hardware. It offers a unique "Multi-Persona" experience, allowing users to interact with archetypal guides, spiritual advisors, or even "Legacy Avatars" created from personal documents.

## Core Value Proposition
- **Sovereign Privacy:** No message, preference, or data ever leaves the device.
- **One-Time Purchase:** A sustainable $0 server-cost business model.
- **Deep Specialization:** 12 distinct archetypes (Mentor, Stoic, Psychologist, etc.) powered by segmented local RAG.
- **Immortality & Legacy:** Tools to digitize personal journals/books into conversational avatars.

## Target User Personas
1. **Privacy Purists:** Users who refuse to share intimate thoughts with big-tech servers.
2. **Growth Seekers:** Individuals looking for structured mentorship (Stoic, Mentor) without recurring costs.
3. **Spiritual Explorers:** Users seeking guidance from specialized religious or philosophical frameworks.
4. **Memory Keepers:** People wishing to interact with a digital reflection of a loved one's writings.

## Functional Requirements

### 1. Offline Inference Engine
- **Models:** Optimized SLMs (Llama 3.2 3B, Gemma 2 2B) quantized to 4-bit.
- **Hardware Acceleration:** Native integration with GPU/NPU via MediaPipe or llama.cpp.
- **Dynamic Downloads:** Background model delivery after initial store installation.

### 2. Multi-RAG Architecture
- **Semantic Search:** Local vector embeddings (all-MiniLM-L6-v2) for context retrieval.
- **Segmented Knowledge:** Each persona accesses a unique, curated local knowledge base.
- **Legacy Uploads:** Support for .txt and .pdf ingestion to create custom avatars.

### 3. Active Learning (Private Profile)
- **Automatic Extraction:** Asynchronous background processing of chat logs to identify user tastes/values.
- **Local Control:** A "Memory" dashboard where users can view, edit, or purge everything the AI has learned.

### 4. Zero-Cost Web Search
- **Local Scraping:** Privacy-preserving extraction from DuckDuckGo HTML to provide real-world context without API fees.

### 5. Constitutional Safety
- **Universal Guardrails:** A mandatory system-level prompt based on the Universal Declaration of Human Rights to prevent harmful discourse.

## Technical Stack
- **Language:** Kotlin (Android Native)
- **Database:** Room (Relational) + ObjectBox (Vector Search)
- **Inference:** MediaPipe LLM Inference / ONNX Runtime
- **Networking:** Jsoup (for local web scraping)

## Design Vision
A "High-Tech Humanist" aesthetic. The UI uses deep indigo surfaces, glassmorphism textures, and soft neural glows to convey both military-grade security and warm, empathetic companionship. Typography is a mix of technical monospaced fonts and elegant serifs.
