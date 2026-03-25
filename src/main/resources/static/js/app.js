console.log("JS файл успешно загружен!");

document.addEventListener("DOMContentLoaded", function() {
    setTimeout(() => {
        const title = document.querySelector('h1');
        if (title) {
            title.style.color = '#e67e22';
            title.innerText += " + JS работает! ✅";
        }
    }, 3000);
});
