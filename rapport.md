# Introduction

![Capture d'écran][rapport]

Voici la sortie de la console.


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
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/Louix27/TP-Jenkins.git'
            }
        }

        stage('Compile') {
            steps {
                sh '''
        mkdir -p build/classes
        javac -cp junit-platform-console-standalone-1.9.3.jar \
              -d build/classes \
              Factorial.java FactorialTest.java
        '''
            }
        }


        stage('Tests') {
            when {
                expression { params.RUN_TESTS }
            }
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
            when {
                expression { params.ENV == 'prod' }
            }
            steps {
                echo "Déploiement en ${ENV}"
            }
        }
    }
}

```
On voit que les tests passent avec succès : 

```bash
Test run finished after 25 ms
[         4 containers found      ]
[         0 containers skipped    ]
[         4 containers started    ]
[         0 containers aborted    ]
[         4 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         0 tests skipped         ]
[         5 tests started         ]
[         0 tests aborted         ]
[         5 tests successful      ]
[         0 tests failed          ]
```


Pipeline Overview :

![Capture d'écran][groovy]

Tests de perturbations :

J'ai modifié un return dans une fonction pour obtenir un test qui fail et on voit bien dans la sortie de la console que des tests échoue.

J'ai modifié ca :
```java
return n + factorial(n - 1);
```
        


```bash
Test run finished after 26 ms
[         4 containers found      ]
[         0 containers skipped    ]
[         4 containers started    ]
[         0 containers aborted    ]
[         4 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         0 tests skipped         ]
[         5 tests started         ]
[         0 tests aborted         ]
[         3 tests successful      ]
[         2 tests failed          ]
```


[rapport]: images/intro.png
[Exo1]: images/Exo1javaFreestyle.png
[groovy]: images/groovy.png
