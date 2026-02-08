# Conteúdo para os Slides da Apresentação

## Slide 1: Introdução
*   **Título**: ToDo List App (Firebase + Jetpack Compose)
*   **Disciplina**: Programação para Dispositivos Móveis (PDM)
*   **Autores**: Leonardo Rodrigues Oliveira Saraiva e André Noro
*   **Objetivo**: Desenvolvimento de aplicativo Android com Autenticação (Firebase Auth), Persistência em Nuvem (Firestore) e Arquitetura MVVM, migrando da solução local (Room) para Nuvem.

## Slide 2: Arquitetura e Tecnologias
*   **MVVM Clean Architecture**: UI (Compose) <- ViewModel <- Repository (Interface) <- Data Source (Firebase).
*   **Tecnologias**:
    *   **Jetpack Compose**: UI Declarativa moderna.
    *   **Firebase Authentication**: Login e Cadastro seguros.
    *   **Cloud Firestore**: Banco NoSQL com sincronização em tempo real (`Flow` e `SnapshotListener`).
    *   **State Management**: `StateFlow` e `Livedata` para reatividade.
*   **Destaque**: Injeção de dependência manual para manter simplicidade e controle.

## Slide 3: Dificuldades Encontradas
*   **Sincronização Assíncrona**: Gerenciar estados de "Loading", "Success" e "Error" nas chamadas de rede foi mais complexo que o acesso síncrono local.
*   **Fluxo de Autenticação**: Garantir o redirecionamento correto (Login -> Home) considerando os delays de verificação de sessão do Firebase.
*   **Estado do Compose**: Lidar com eventos de navegação (como o `popBackStack` após salvar) dentro dos efeitos colaterais (`LaunchedEffect`) do Compose exigiu cuidado para evitar bugs de recomposição.

## Slide 4: Uso de LLMs (Gemini)
*   **LLM utilizada**: Gemini 2.0 Flash (via Google DeepMind Agent).
*   **Prompts Principais**:
    *   "Analise a arquitetura MVVM do projeto base e migre a camada de dados para usar Firestore."
    *   "Corrija o bug de navegação onde a tela travava após salvar a tarefa."
    *   "Gere a documentação técnica seguindo o padrão KDoc."
*   **Opinião do Grupo**: A IA atuou como um par programador eficiente, acelerando a escrita de código "boilerplate" (configuração do Firebase) e ajudando a entender erros de compilação complexos. O aprendizado foi reforçado ao revisar e adaptar o código gerado.

