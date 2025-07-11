# WeMovies – Desafio Mobile Nativo

Este projeto é parte do desafio de desenvolvimento mobile nativo para a vaga de desenvolvedor pleno.

---

## Sobre o App

O WeMovies é um e-commerce mobile simplificado que consome uma API externa para exibir uma lista de filmes. Permite adicionar ao carrinho, visualizar subtotal, remover produtos e finalizar a compra com um fluxo completo.
workflows
---

## Funcionalidades Implementadas

- Splash + Load State com feedback visual
- Home com consumo de API (Retrofit) e exibição de filmes (RecyclerView)
- Carrinho com:
  - Itens adicionados e atualizados dinamicamente
  - Somatório do valor total
  - Empty state com redirecionamento
  - Scroll total com header, lista e footer bem integrados
- Finalização de compra com tela de sucesso
- Empty State na Home, caso a API retorne vazia

---

## Arquitetura

O app segue o padrão MVVM, utilizando:

- ViewModel com LiveData para gerenciamento de estado
- Separação clara entre camadas (ui, network, models, viewmodels)
- Componentização de itens visuais via RecyclerView com múltiplos tipos de item (Header, Produto, Footer)
- Glide para carregamento eficiente de imagens
- Retrofit para requisições HTTP
- WindowInsetsCompat para ajuste inteligente da interface em dispositivos com gestos ou barra de navegação

---

## Decisão Técnica

Apesar do tempo reduzido, optei por construir a base do projeto com uma arquitetura sólida que permita escalar com segurança em ambiente de produção. Isso inclui:

- Desacoplamento da UI e lógica de dados
- Componentes reutilizáveis
- Estrutura organizada por feature (ex: ui.home, ui.carrinho)
- Base pronta para integração com Hilt ou Koin, se necessário

Em contextos reais, injeção de dependência como Hilt seria aplicada. Neste desafio, optei por manter a injeção manual para priorizar entrega funcional e código limpo no prazo estabelecido.

---

## Testes Manuais Realizados

- Adicionar e remover itens do carrinho
- Teste de scroll e visibilidade do botão de finalizar
- Simulação de API vazia (empty state)
- Navegação completa entre telas
- Responsividade visual (insets, diferentes resoluções)

---

## Observações

- Projeto entregue em 48 horas
- Codigo prontamente escalavel
- Código modular, legível e de fácil manutenção

---

## Estrutura de pastas

wemovie-ecommerce/
├── ui/
│   ├── home/
│   ├── carrinho/
│   ├── finalizar/
│   ├── perfil/
│   └── LoadState/
├── ViewModels/
├── Models/
├── NetWork/
├── utils/
├── Error/
├── yaml/
├── MainActivity.kt
└── README.md

---

## API

https://wefit-movies.vercel.app/api/movies
"""
