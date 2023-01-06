htmx.on("#akar", "htmx:oobBeforeSwap", event => {
    // https://stackoverflow.com/a/2024618/8812880 modified
    var queryString = '?reload=' + new Date().getTime();
    var links = document.getElementsByTagName("link")
    for (const idx in links) {
        if (Object.hasOwnProperty.call(links, idx)) {
            links[idx].href = links[idx].href.replace(/\?.*|$/, queryString)
        }
    }
    console.log("css refreshed!")
});

console.log("cssRefresher registered!")