# ToDo List App (Firebase + MVVM Clean Architecture)

Este aplicativo é uma implementação de uma Lista de Tarefas (ToDo List) Android nativa utilizando Kotlin e Jetpack Compose. O projeto foi desenvolvido como parte da disciplina de Programação para Dispositivos Móveis (PDM) - Trabalho 3.

## Funcionalidades

*   **Autenticação**: Login e Cadastro de usuários utilizando Firebase Authentication (Email/Senha).
*   **Persistência em Nuvem**: As tarefas são salvas no Firebase Firestore, permitindo sincronização e persistência remota.
*   **Lista Privada**: Cada usuário visualiza apenas as suas próprias tarefas.
*   **Gerenciamento de Tarefas**: Adicionar, Editar (Título/Descrição), Marcar como Concluída e Excluir tarefas.
*   **Interface Moderna**: Utiliza Material Design 3 e Jetpack Compose.

## Arquitetura

O projeto segue estritamente os padrões de arquitetura **MVVM (Model-View-ViewModel)** com **Clean Architecture** simplificada e **Repository Pattern**, inspirado no projeto de referência.

*   **UI Layer**: Composta por Telas (`Screen`) e Componentes (`Composable`). O estado é gerenciado por `ViewModels`.
*   **Domain Layer**: Contém as entidades puras (`Todo`, `Result`), desacopladas de frameworks.
*   **Data Layer**: Implementa os Repositórios (`AuthRepositoryImpl`, `TodoRepositoryImpl`) que comunicam com o Firebase.
*   **Injeção de Dependência**: Utiliza injeção manual (Manual DI) via `AppContainer`, evitando a complexidade de frameworks como Hilt para este escopo, mantendo o controle explícito.

## Configuração do Projeto

Para rodar este projeto, é necessário configurar o Firebase:

1.  Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
2.  Adicione um app Android com o pacote: `com.example.todolistfirebase`.
3.  Baixe o arquivo `google-services.json` e coloque-o na pasta `app/` deste projeto.
4.  No console, ative **Authentication** (Provedor Email/Senha).
5.  No console, crie um **Firestore Database** e configure as regras de segurança.

## Decisões Técnicas

*   **Jetpack Compose**: Escolhido pela produtividade e facilidade de criação de UIs reativas (Declarative UI).
*   **Firebase**: Escolhido para atender aos requisitos de autenticação e banco de dados em nuvem sem necessidade de backend próprio.
*   **Type-Safe Navigation**: Utilização da nova biblioteca de navegação do Compose para garantir segurança de tipos nos argumentos das rotas.

## Autores

*   Leonardo Rodrigues Oliveira Saraiva
*   André Noro
