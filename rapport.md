# Introduction

![Capture d'écran][rapport]

Voici la sortie de la console.

Exemple de commande utilisée :

```bash
echo "Mon premier exemple Jenkins !"
date
```

## Exercice 1 — Java

### Java (Freestyle)

Compilation :

```bash
echo "=== Compilation Java ==="
mkdir -p build/classes
javac -d build/classes *.java
```

Lancement des tests (optionnel) :

```bash
if [ "$RUN_TESTS" = "true" ]; then
  echo "=== Lancement des tests ==="
  java -jar junit-platform-console-standalone-1.9.3.jar \
    -cp build/classes \
    --scan-classpath
else
  echo "=== Tests désactivés ==="
fi
```

Sortie de la console :

![Capture d'écran][Exo1]

### Java (Pipeline Groovy)

Pipeline utilisé :

```groovy
pipeline {
    agent any

    parameters {
        booleanParam(name: 'RUN_TESTS', defaultValue: true)
        choice(name: 'ENV', choices: ['test', 'dev', 'prod'])
    }

    stages {
        stage('Clean') {
            steps { deleteDir() }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Louix27/TP-Jenkins.git'
            }
        }

        stage('Compile') {
            steps {
                sh '''
                mkdir -p build/classes
                javac -d build/classes *.java
                '''
            }
        }

        stage('Tests') {
            when { expression { params.RUN_TESTS } }
            steps {
                sh '''
                java -jar junit-platform-console-standalone-1.9.3.jar \
                  -cp build/classes \
                  --scan-classpath
                '''
            }
        }

        stage('Jar') {
            steps {
                sh '''
                mkdir -p dist
                jar cf dist/factorielle.jar -C build/classes .
                '''
            }
        }

        stage('Deploy') {
            when { expression { params.ENV == 'prod' } }
            steps { echo "Déploiement en ${ENV}" }
        }
    }
}
```

Pipeline Overview :

![Capture d'écran][groovy]

[rapport]: intro.png
[Exo1]: Exo1javaFreestyle.png
[groovy]: groovy.png
