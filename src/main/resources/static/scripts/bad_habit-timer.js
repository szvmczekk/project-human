(function () {
    function pad(n) {
        return String(n).padStart(2, "0");
    }

    function formatDuration(ms) {
        if (ms < 0) ms = 0;

        const totalSeconds = Math.floor(ms / 1000);
        const days = Math.floor(totalSeconds / 86400);
        const hours = Math.floor((totalSeconds % 86400) / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;

        // 0d 05:12:09 albo 05:12:09
        if (days > 0) return `${days}d ${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
        return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
    }

    function parseLocalIso(isoLike) {
        // isoLike: "2026-01-17T13:42:10"
        // Date("YYYY-MM-DDTHH:mm:ss") w praktyce działa jako local w większości przeglądarek,
        // ale robimy parse “na pewniaka”.
        const [datePart, timePart] = isoLike.split("T");
        if (!datePart || !timePart) return null;

        const [y, m, d] = datePart.split("-").map(Number);
        const [hh, mm, ss] = timePart.split(":").map(Number);

        if ([y, m, d, hh, mm].some(Number.isNaN)) return null;
        return new Date(y, (m - 1), d, hh, mm, ss || 0);
    }

    function tick() {
        const now = Date.now();
        document.querySelectorAll("[data-live-timer]").forEach((el) => {
            const startRaw = el.getAttribute("data-start");
            if (!startRaw) return;

            const startDate = parseLocalIso(startRaw);
            if (!startDate) return;

            const diff = now - startDate.getTime();
            el.textContent = formatDuration(diff);
        });
    }

    // start
    tick();
    setInterval(tick, 1000);
})();
