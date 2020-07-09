install:
	npm install --no-audit
	printf "#!/bin/sh\n\tmake format && make lint" > .git/hooks/pre-push
	chmod +x .git/hooks/pre-push

format: install
	npx prettier --write "src/main/webapp/**/*.{html,css,js,ts}"
	mvn com.coveo:fmt-maven-plugin:format -Dverbose=true

lint: install
	npx eslint --ignore-pattern=**/target --ignore-pattern=**/node_modules --ignore-pattern=**/dist --ignore-pattern=**/*.conf.js --ignore-pattern=**/webpack.config.js **/*.ts
	mvn org.apache.maven.plugins:maven-checkstyle-plugin:check -Dverbose=true

dev: install
	npm run dev

prod: install
	npm run prod

deploy: install
	npm run deploy
