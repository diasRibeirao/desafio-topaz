const form =
    document.getElementById('shorten-form');

const urlInput =
    document.getElementById('url');

const aliasInput =
    document.getElementById('alias');

const resultBox =
    document.getElementById('result');

const errorBox =
    document.getElementById('error');

const shortUrlLink =
    document.getElementById('short-url-link');

const copyBtn =
    document.getElementById('copy-btn');

const submitBtn =
    document.getElementById('submit-btn');


form.addEventListener(
    'submit',
    async function (event) {

        event.preventDefault();

        limparMensagens();

        const url =
            urlInput.value.trim();

        const alias =
            aliasInput.value.trim();

        setLoading(true);

        try {

            const payload = {
                url: url
            };

            if (alias) {
                payload.alias = alias;
            }

            const response =
                await fetch(
                    'api/urls',
                    {
                        method: 'POST',

                        headers: {
                            'Content-Type':
                                'application/json'
                        },

                        body:
                            JSON.stringify(payload)
                    }
                );

            const data =
                await lerResposta(response);

            if (!response.ok) {

                showError(
                    obterMensagemErro(data)
                );

                return;
            }

            const urlCurta =
                data.urlCurta ||
                data.shortUrl;

            if (!urlCurta) {

                showError(
                    'O servidor não retornou a URL curta.'
                );

                return;
            }

            exibirResultado(urlCurta);

        } catch (error) {

            showError(
                'Falha de comunicação com o servidor.'
            );

        } finally {

            setLoading(false);
        }
    }
);


copyBtn.addEventListener(
    'click',
    async function () {

        const url =
            shortUrlLink.href;

        if (!url) {
            return;
        }

        try {

            if (navigator.clipboard &&
                navigator.clipboard.writeText) {

                await navigator.clipboard
                    .writeText(url);

                mostrarCopiado();

                return;
            }

            copiarFallback(url);

        } catch (error) {

            copiarFallback(url);
        }
    }
);


async function lerResposta(response) {

    const contentType =
        response.headers.get(
            'content-type'
        );

    if (contentType &&
        contentType.includes(
            'application/json'
        )) {

        return await response.json();
    }

    const texto =
        await response.text();

    return {
        mensagem: texto
    };
}


function obterMensagemErro(data) {

    if (!data) {
        return 'Não foi possível gerar o link.';
    }

    return data.mensagem ||
        data.message ||
        data.error ||
        'Não foi possível gerar o link.';
}


function exibirResultado(urlCurta) {

    shortUrlLink.href =
        urlCurta;

    shortUrlLink.textContent =
        urlCurta;

    show(resultBox);
}


function copiarFallback(texto) {

    const textarea =
        document.createElement(
            'textarea'
        );

    textarea.value =
        texto;

    textarea.style.position =
        'fixed';

    textarea.style.opacity =
        '0';

    document.body.appendChild(
        textarea
    );

    textarea.focus();
    textarea.select();

    try {

        const copiado =
            document.execCommand(
                'copy'
            );

        if (!copiado) {

            showError(
                'Não foi possível copiar o link.'
            );

            return;
        }

        mostrarCopiado();

    } catch (error) {

        showError(
            'Não foi possível copiar o link.'
        );

    } finally {

        document.body.removeChild(
            textarea
        );
    }
}


function mostrarCopiado() {

    const textoOriginal =
        copyBtn.textContent;

    copyBtn.textContent =
        'Copiado!';

    copyBtn.disabled =
        true;

    setTimeout(
        function () {

            copyBtn.textContent =
                textoOriginal;

            copyBtn.disabled =
                false;

        },
        1500
    );
}


function setLoading(loading) {

    submitBtn.disabled =
        loading;

    urlInput.disabled =
        loading;

    aliasInput.disabled =
        loading;

    submitBtn.textContent =
        loading
            ? 'Gerando...'
            : 'Gerar link curto';
}


function limparMensagens() {

    hide(resultBox);
    hide(errorBox);

    errorBox.textContent =
        '';

    shortUrlLink.textContent =
        '';

    shortUrlLink.removeAttribute(
        'href'
    );
}


function showError(message) {

    errorBox.textContent =
        message;

    hide(resultBox);

    show(errorBox);
}


function show(element) {

    element.classList.remove(
        'hidden'
    );
}


function hide(element) {

    element.classList.add(
        'hidden'
    );
}