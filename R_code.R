library(tseries)
library(forecast)
require(caret)
install.packages("MLmetrics")
library(MLmetrics)
library(boot)
library(SummarizedExperiment)

install.packages("tidyverse") # коллекция пакетов от Hadley Wickham
install.packages("knitr") # взаимодействие R-LaTeX и R-markdown
install.packages("rmarkdown") # взаимодействие R-markdown
install.packages("xtable") # перевод таблиц в LaTeX
install.packages("texreg") # сравнение моделей в LaTeX
install.packages("pander") # перевод таблиц в markdown
install.packages("memisc") # перевод таблиц в markdown
install.packages('huxtable') # красивые таблички для latex/markdown/html
install.packages("lmtest") # тесты в линейных моделях
install.packages("sandwich") # оценки ковариационной матрицы робастные к гетероскедастичности
install.packages("erer") # подборка пакетов для эмпирических исследований
install.packages("AUC") # подсчёт показателя AUC
install.packages("mfx") # для предельных эффектов в logit/probit
install.packages("estimatr") # модели с робастными стандартными ошибками
install.packages("GGally") # матрица диаграмм рассеяния
install.packages("lattice") # конкурент ggplot2
install.packages("vcd") # мозаичный график
install.packages("hexbin") # график из шестиугольников
install.packages("sjPlot") # визуализация результатов МНК
install.packages("factoextra") # визуализация для метода главных компонент и не только
install.packages("reshape2") # длинные <-> широкие таблицы
install.packages("psych") # описательные статистики
install.packages("skimr") # описательные статистики
install.packages("glmnet") # LASSO
install.packages("HSAUR")
install.packages("sgof")
install.packages("car") # для тестирования линейных гипотез, подсчёта vif
install.packages("spikeslab") # байесовская регрессия пик-плато
install.packages("quantreg") # квантильная регрессия
install.packages("MCMCpack") # набор моделей с байесовским подходом
install.packages("devtools") # разработка пакетов
install.packages("caret") # подбор параметров с помощью кросс-валидации
install.packages("AER")
install.packages("ivpack") # интсрументальные переменные
install.packages("zoo") # нерегулярные временные ряды
install.packages("xts") # еще ряды
install.packages("forecast") # ARMA, экспоненциальное сглаживание
install.packages("rugarch") # не используется в курсе, хорош для GARCH
install.packages("quantmod") # загрузка с finance.google.com
install.packages("Quandl") # загрузка с Quandl
# non-CRAN packages:
devtools::install_github("bdemeshev/sophisthse", force = TRUE) # read data from sophist.hse.ru
devtools::install_github("bdemeshev/rlms", force = TRUE) # авточистка данных RLMS
# дополнение к quantmod для загрузки данных с finam.ru
install.packages("rusquant", repos = "http://r-forge.r-project.org", type = "source")
if (!require("BiocManager", quietly = TRUE))
  install.packages("BiocManager")

install.packages("httr2")
BiocManager::install("TCGAbiolinks", force = TRUE)

if (!require("SummarizedExperiment")) install.packages("SummarizedExperiment")
if (!require("ggplot2")) install.packages("ggplot2")
if (!require("dplyr")) install.packages("dplyr")

install.packages('BiocManager')
library(BiocManager)
install('TCGAbiolinks', force = TRUE)
library(TCGAbiolinks)
install.packages('magrittr')
library(magrittr)
install('org.Hs.eg.db')
library("org.Hs.eg.db", character.only = TRUE)

library(TCGAbiolinks)
library(SummarizedExperiment)
library(ggplot2)
library(dplyr)

luad_query <- GDCquery(project = "TCGA-PRAD",
                       data.category = "Transcriptome Profiling",
                       data.type = "Gene Expression Quantification"
)
GDCdownload(luad_query)

# data preparing
luad_data <- GDCprepare(luad_query)

# to get a list of gene types (as.vector)
#luad_data@rowRanges@elementMetadata@listData$gene_type

as.vector(as.data.frame(luad_query[[1]][[1]])$sample_type) %>%
  table()

# data preparing
luad_data <- GDCprepare(luad_query)
luad_expMat <- assay(luad_data)

# save the matrix
write.csv(luad_expMat, 'luad_expMat.csv')

# converting gene names
genes_ensg <- gsub("\\..*","", rownames(luad_expMat))
genes_symb <- mapIds(org.Hs.eg.db, keys = genes_ensg,
                     keytype ="ENSEMBL", column ="SYMBOL") %>%
  as.vector()

# our set of the genes
set_gen <- c("STAT2", "STAT1", "JAK2", "STAT5A", "JAK1", "SRC", "E2F8", "E2F7", "E2F6", "E2F5", "E2F4", "E2F2", "E2F1", "CDKN2A",
             "CDKN2B", "CDK4", "MYC", "CCND1", "CDKN1B", "CCND2", "CDK6", "CDKN1A", "CDC25A", "CDK2", "CCNE1", "STAT3", "STAT5B",
             "E2F3", "CDK1", "CCNB1", "CCNA1", "RBL2", "RBL1", "RB1")


# to take information only about genes from our set
luad_expMat %>%
  as.data.frame() %>%
  .[genes_symb %in% gene_names, ] %>%
  na.omit()

project <- "TCGA-PRAD"

# Получение данных о экспрессии генов
data <- GDCquery(project = project,
                 data.category = "Transcriptome Profiling",
                 data.type = "Gene Expression Quantification"
)

# Загрузка данных
data <- GDCdownload(data, method = "wget", files.type = "rse", directory = "TCGA-PRAD")

# Создание объекта SummarizedExperiment
rse <- readRDS(data)

# Вычисление среднего значения экспрессии генов по образцам
gene_expression <- rowMeans(assay(rse))

# Сортировка генов по среднему значению экспрессии
top_genes <- sort(gene_expression, decreasing = TRUE)

# Вывод 20 самых активных генов
head(top_genes, n = 20)

# Построение графика топ-20 генов
top_genes_df <- data.frame(gene = names(top_genes[1:20]),
                           expression = top_genes[1:20])

ggplot(top_genes_df, aes(x = reorder(gene, expression), y = expression)) +
  geom_bar(stat = "identity") +
  labs(title = "Топ-20 самых активных генов в TCGA-PRAD",
       x = "Ген", y = "Среднее значение экспрессии") +
  coord_flip()

# Построение гистограммы распределения экспрессии генов
ggplot(data.frame(expression = gene_expression), aes(x = expression)) +
  geom_histogram() +
  labs(title = "Распределение экспрессии генов в TCGA-PRAD",
       x = "Экспрессия генов", y = "Частота")