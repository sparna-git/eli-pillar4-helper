# eli-pillar4-helper
Helper application for ELI Pillar IV protocol

## Test command

```sh
java -jar eli/pillar4-helper/pillar4-helper-app/target/pillar4-helper-app-1.0.3-onejar.jar sparql2pillar4 \
 -ah header.atom \
 -ao atom_output.xml \
 --endpoint https://data.legilux.public.lu/sparqlendpoint \
 --query eli/pillar4-helper/pillar4-helper-app/documentation/query-legilux.rq \
 --sitemapBaseUrl http://data.legilux.public.lu/eli/ \
 --sitemapOutput output
```