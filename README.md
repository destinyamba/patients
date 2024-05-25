This is a Spring Boot project using kotlin and gradle.

## Getting Started

### Prerequisites

- Java 17
- Gradle
- Helm (for managing Kubernetes packages)
- Sops (for decrypting secrets)

### Installing the prerequisites and decrypting secrets

```bash
brew install helm
brew install sops
helm plugin install https://github.com/jkroepke/helm-secrets  
brew install gpg # For generating keys
gpg --import private-key.gpg # import the key used to encrypt the secrets
export GPG_TTY=$(tty)   # Set the tty
sops --decrypt secrets.yaml > ../src/main/resources/application.yml   # Decrypt the secrets file

When the popup promt appears, type 'patients' and press enter.
```

## Encrypting secrets

- Navigate to helm-config/secrets.yaml
- Decrypt the secrets file
- Add the secrets

```bash
gpg --list-secret-keys --keyid-format LONG   # Get the key id
export GPG_TTY=$(tty)   # Set the tty
sops --encrypt --in-place --pgp <pgp-id> secrets.yaml   # Encrypt the secrets file
# It should look like this:  sops --encrypt --in-place --pgp 3SD89UFS89DF7S8DF7SD393898SDF789SDF99 secrets.yaml      
```
