/**
 * Chart.js initialization for the AI Interview Platform dashboard.
 * Called after Chart.js CDN is loaded.
 */

window.initCharts = function (chartLabels, chartScores, domainLabels, domainData) {

    const PALETTE = ['#6366f1','#8b5cf6','#a78bfa','#38bdf8','#10b981','#f59e0b','#ef4444'];
    const GRID = 'rgba(99,102,241,0.08)';
    const TEXT_COLOR = '#94a3b8';

    Chart.defaults.color = TEXT_COLOR;
    Chart.defaults.font.family = "'Inter', sans-serif";

    // ── 1. Score Trend Line Chart ─────────────────────────────────────────────
    const trendEl = document.getElementById('scoreTrendChart');
    if (trendEl && chartLabels && chartScores) {
        new Chart(trendEl, {
            type: 'line',
            data: {
                labels: chartLabels,
                datasets: [{
                    label: 'Score (%)',
                    data: chartScores,
                    borderColor: '#6366f1',
                    backgroundColor: 'rgba(99,102,241,0.08)',
                    pointBackgroundColor: '#818cf8',
                    pointBorderColor: '#6366f1',
                    pointRadius: 5,
                    pointHoverRadius: 8,
                    borderWidth: 2.5,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        backgroundColor: '#0f0f2e',
                        borderColor: 'rgba(99,102,241,0.3)',
                        borderWidth: 1,
                        titleColor: '#e2e8f0',
                        bodyColor: '#94a3b8',
                        callbacks: {
                            label: ctx => ` Score: ${ctx.parsed.y.toFixed(1)}%`
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { color: GRID },
                        ticks: { maxTicksLimit: 8 }
                    },
                    y: {
                        min: 0,
                        max: 100,
                        grid: { color: GRID },
                        ticks: { callback: v => v + '%' }
                    }
                }
            }
        });
    }

    // ── 2. Domain Distribution Doughnut Chart ─────────────────────────────────
    const domainEl = document.getElementById('domainChart');
    if (domainEl && domainLabels && domainLabels.length > 0) {
        new Chart(domainEl, {
            type: 'doughnut',
            data: {
                labels: domainLabels,
                datasets: [{
                    data: domainData,
                    backgroundColor: PALETTE,
                    borderColor: '#07071a',
                    borderWidth: 3,
                    hoverOffset: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 16,
                            usePointStyle: true,
                            pointStyle: 'circle',
                            font: { size: 11 }
                        }
                    },
                    tooltip: {
                        backgroundColor: '#0f0f2e',
                        borderColor: 'rgba(99,102,241,0.3)',
                        borderWidth: 1,
                        titleColor: '#e2e8f0',
                        bodyColor: '#94a3b8'
                    }
                },
                cutout: '70%'
            }
        });
    }
};

// ── Score ring animation (result page) ────────────────────────────────────────
window.animateScoreRing = function (score) {
    const circle = document.getElementById('scoreCircle');
    if (!circle) return;
    const radius = circle.r.baseVal.value;
    const circumference = 2 * Math.PI * radius;
    circle.style.strokeDasharray = circumference;
    circle.style.strokeDashoffset = circumference;

    setTimeout(() => {
        const offset = circumference - (score / 100) * circumference;
        circle.style.strokeDashoffset = offset;
    }, 200);
};
