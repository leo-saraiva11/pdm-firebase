# Documentação Técnica - ToDo List App

## 1. Modelo de Dados

O modelo de dados principal é a entidade `Todo`, que representa uma tarefa. Ela foi desenhada para ser simples e serializável para o Firestore.

```kotlin
data class Todo(
    val id: String = "",           // ID único gerado pelo Firestore
    val title: String = "",        // Título da tarefa
    val description: String? = null, // Descrição opcional
    val isCompleted: Boolean = false,// Estado de conclusão
    val userId: String = ""        // ID do usuário dono da tarefa (Foreign Key lógica)
)
```

Além disso, utilizamos uma classe `Result<T>` (Sealed Class) para encapsular os retornos das operações assíncronas, permitindo tratar estados de Sucesso, Erro e Carregamento de forma elegante na UI.

## 2. Implementação da Persistência (Firebase)

A persistência foi migrada do Room (SQL local) para o **Firebase Firestore** (NoSQL em nuvem).

*   **Coleção**: Todas as tarefas são armazenadas em uma única coleção chamada `todos`.
*   **Filtragem**: A segurança e a privacidade são garantidas filtrando as consultas pelo campo `userId`. O `AuthRepository` fornece o ID do usuário logado, que é usado pelo `ListViewModel` para solicitar apenas as tarefas daquele usuário ao `TodoRepository`.
*   **Tempo Real**: Utilizamos `SnapshotListener` do Firestore (convertido para Kotlin `Flow` via `callbackFlow`) para que a lista de tarefas seja atualizada em tempo real na tela do usuário assim que houver mudança no banco.

### Autenticação
A autenticação utiliza o SDK do **Firebase Auth**. O estado de autenticação é observado através de um `Flow`, permitindo que o app navegue automaticamente para a tela de Login caso o usuário faça logout ou sua sessão expire.

## 3. Melhorias Futuras

Para versões futuras, sugerimos as seguintes melhorias:

1.  **Modo Offline (Offline Support)**: O Firestore já possui suporte a cache offline, mas é necessário habilitar e tratar explicitamente a sincronização para garantir uma experiência robusta sem internet.
2.  **Injeção de Dependência Automatizada**: Para escalar o projeto, migrar a Manual DI (`AppContainer`) para **Hilt/Dagger** reduziria o código boilerplate de instanciação.
3.  **Testes Automatizados**: Implementar testes unitários para as ViewModels e Repositórios (usando mocks do Firebase) e testes de UI com Espresso/Compose Test.
4.  **Categorias e Prioridades**: Adicionar campos no modelo `Todo` para permitir categorizar e priorizar tarefas.
5.  **Splash Screen**: Implementar a API de Splash Screen do Android 12+ para uma inicialização mais fluida enquanto verifica o estado de login.
