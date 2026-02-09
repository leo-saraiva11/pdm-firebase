# Documentação Técnica - ToDo List App

## 1. Introdução
Este documento descreve a implementação técnica do aplicativo "ToDo List Firebase", desenvolvido para a disciplina de Programação para Dispositivos Móveis. O objetivo principal foi migrar uma solução de lista de tarefas local para uma arquitetura baseada em nuvem, utilizando a suíte Firebase.

## 2. Modelo de Dados
O modelo de dados foi desenhado para ser leve e compatível com estruturas NoSQL (document-oriented).

### Entidade `Todo`
A classe principal `Todo` representa uma tarefa individual.
```kotlin
data class Todo(
    val id: String = "",           // Identificador único do documento no Firestore
    val title: String = "",        // Tíulo da tarefa (Obrigatório)
    val description: String? = null, // Detalhamento (Opcional)
    val isCompleted: Boolean = false,// Status de conclusão
    val userId: String = ""        // ID do usuário proprietário (Autenticação)
)
```
*   **Decisão de Design**: A inclusão do campo `userId` permite que todos os *todos* sejam armazenados em uma única coleção raiz (`todos`), simplificando as regras de segurança e consultas. Cada consulta filtra explicitamente por este campo.

### Wrapper `Result<T>`
Para gerenciar a natureza assíncrona das operações de rede, utilizamos uma *Sealed Class* `Result`.
*   `Success<T>`: Contém o dado retornado.
*   `Error`: Contém a exceção gerada.
*   `Loading`: Indica operação em andamento (útil para exibir *spinners* na UI).

## 3. Implementação da Persistência
A persistência utiliza o **Google Cloud Firestore**. Diferente do Room (SQL), o Firestore é um banco de dados NoSQL em tempo real.

### Camada de Dados (Repository Pattern)
A comunicação com o Firebase é isolada na classe `TodoRepositoryImpl`.
*   **Leitura (Real-time)**: Utilizamos `addSnapshotListener` convertido para `callbackFlow`. Isso garante que qualquer alteração no servidor (feita por outro dispositivo, por exemplo) seja refletida imediatamente na UI do app sem necessidade de "pull-to-refresh".
*   **Escrita**: Operações de adicionar (`add`), atualizar (`set`) e deletar (`delete`) são funções `suspend` que retornam `Result<Unit>`, permitindo tratamento de erros granular.

### Autenticação
A integração com **Firebase Authentication** gerencia o ciclo de vida do usuário. O `AppContainer` fornece uma instância singleton de `AuthRepository`, que expõe o estado atual do usuário como um `Flow<String?>`. Isso permite que a `MainActivity` monitore a sessão e redirecione o usuário para a tela de Login ou Lista automaticamente.

## 4. Interface do Usuário (UI/UX)
Desenvolvida 100% em **Jetpack Compose**.
*   **Navegação**: Utiliza `Navigation Compose` com `Type-Safe Routes` (Kotlin Serialization) para passar argumentos.
*   **Optimistic UI (UI Otimista)**: Para garantir uma experiência fluida, implementamos um padrão de *Fire-and-Forget* na criação/edição de tarefas. Ao salvar, a interface reage imediatamente (fechando a tela), enquanto a sincronização com o Firestore ocorre em segundo plano. Isso elimina a sensação de latência em conexões lentas.
*   **Feedback**: Snackbars são usadas para notificar erros (ex: falha ao carregar lista).
*   **Estilo**: Material Design 3.

## 5. Melhorias Futuras
Para evoluir o projeto numa versão 2.0, sugerimos:

1.  **Suporte Offline Robusto**:
    *   Embora o Firestore tenha cache offline, o app deve explicitar ao usuário quando ele está sem internet e quais dados estão pendentes de sincronização.
2.  **Testes Automatizados**:
    *   Implementar testes unitários para a lógica de `ViewModel` (usando mocks para os Repositórios).
    *   Criar testes de UI (Espresso/Compose Test) para validar fluxos críticos como Login e Criação de Tarefa.
3.  **Injeção de Dependência Automática (Hilt)**:
    *   Substituir o `AppContainer` manual pelo Hilt facilitaria a gestão de escopos (ex: Singleton vs ViewModelScoped) e a injeção em testes.
4.  **Funcionalidades Extras**:
    *   Categorias de tarefas (Trabalho, Pessoal, etc.).
    *   Lembretes via Push Notification (Firebase Cloud Messaging).

---
*Documentação gerada com auxílio da LLM Gemini, revisada e validada pelos autores.*

