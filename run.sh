java -jar eli/pillar4-helper/pillar4-helper-app/target/pillar4-helper-app-1.0.5-onejar.jar \
atomheader \
--output header.atom

java -jar eli/pillar4-helper/pillar4-helper-app/target/pillar4-helper-app-1.0.5-onejar.jar \
sparql2pillar4 \
--atomHeader header.atom \
--atomOutput atom_output.xml \
--endpoint https://data.legilux.public.lu/sparqlendpoint \
--query eli/pillar4-helper/pillar4-helper-app/documentation/query-legilux.rq \
--sitemapBaseUrl http://data.legilux.public.lu/eli \
--sitemapOutput output