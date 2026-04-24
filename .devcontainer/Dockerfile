FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

# Fix expired Yarn GPG key then install Docker and Maven
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | gpg --dearmor \
    | tee /usr/share/keyrings/yarn-archive-keyring.gpg > /dev/null \
        && echo "deb [signed-by=/usr/share/keyrings/yarn-archive-keyring.gpg] https://dl.yarnpkg.com/debian/ stable main" \
            | tee /etc/apt/sources.list.d/yarn.list \
                && apt-get update \
                    && apt-get install -y docker.io docker-compose maven \
                        && apt-get clean -y