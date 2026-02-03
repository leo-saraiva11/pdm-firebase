# Conteúdo para os Slides da Apresentação

## Slide 1: Capa e Introdução
*   **Título**: ToDo List App com Firebase
*   **Disciplina**: Programação para Dispositivos Móveis (PDM)
*   **Integrantes**: [Nome 1] e [Nome 2]
*   **Objetivo**: Migrar um app de lista de tarefas local para a nuvem, implementando Autenticação e Banco de Dados em tempo real.

## Slide 2: Arquitetura e Tecnologias
*   **MVVM + Repository Pattern**: Separação clara entre UI, Lógica e Dados.
*   **Jetpack Compose**: Interface declarativa moderna.
*   **Firebase Auth**: Gerenciamento seguro de sessões de usuário.
*   **Firestore**: Banco de dados NoSQL com atualizações em tempo real (`Flow`).
*   **Código**: Destaque para a `Manual Dependency Injection` e a organização em camadas (`ui`, `domain`, `data`).

## Slide 3: Dificuldades Encontradas
*   **Assincronismo**: Gerenciar o estado de carregamento e erros das chamadas de rede (diferente do Room que é local e rápido).
*   **Coroutines & Flow**: Entender como converter os callbacks do Firebase (listeners) para Kotlin Flows reativos.
*   **Configuração do Ambiente**: Configurar corretamente as chaves e dependências do Google Services no Gradle.

## Slide 4: Uso de LLMs (Inteligência Artificial)
*   **LLM Utilizada**: Gemini (via Google DeepMind Agent).
*   **Como ajudou**:
    *   Gerou a estrutura inicial do projeto seguindo estritamente os padrões de arquitetura solicitados.
    *   Acelerou a escrita do código repetitivo (biolerplate) de configuração do Firebase.
    *   Explicou conceitos de Clean Architecture aplicados ao Android.
*   **Prómpth Exemplo**: "Analise a arquitetura desse repositório X e crie um novo projeto seguindo os mesmos padrões, mas integrando Firebase".
*   **Opinião**: A LLM atuou como um "Pair Programmer" experiente, garantindo que boas práticas fossem seguidas desde o início, permitindo que focássemos na lógica de negócio e testes.
