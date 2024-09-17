# MANELISI LUTHULI AND ZENANDE MANGCIPU

# STA2005S ED ASSIGNMENT

# Install the 'readxl' package if it's not already installed
if (!requireNamespace("readxl", quietly = TRUE)) {
  install.packages("readxl")
}

# Load the 'readxl' package
library(readxl)

# Read in data from Experimental Design
Data <- read_excel("ExperimentalDesign.xlsx")


# Random Block Design Model, ANOVA
m1 <- aov(`Time(Miliseconds)` ~ Array_Size + Sorting_Algorithm, data = Data)
summary(m1)


# Estimates

model.tables(m1, "means")

model.tables(m1, se = T)


# Compare means using Bonferroni

pairwise_result <- pairwise.t.test(Data$`Time(Miliseconds)`, Data$Sorting_Algorithm, p.adjust.method = "bonferroni") # Default significance level of 5%

# View pairwise comparison results
print(pairwise_result)


# Boxplot of all the algorithms

boxplot(`Time(Miliseconds)` ~ Sorting_Algorithm, data = Data, las = 1,
        ylab = "Time(Miliseconds)", xlab = "Sorting Algorithm")


# Remove BubbleSort from the data
filtered_data <- subset(Data, Sorting_Algorithm != "BubbleSort")


# Create the boxplot with the remaining sorting algorithms without BubbleSort
boxplot(`Time(Miliseconds)` ~ Sorting_Algorithm, data = filtered_data, las = 1,
        ylab = "Time(Miliseconds)", xlab = "Sorting Algorithm")


# Create boxplot of the blocks
boxplot(`Time(Miliseconds)` ~ Array_Size, data = Data, las = 1,
        ylab = "Time(Miliseconds)", xlab = "Array Size")

# Create boxplot of the blocks without BubbleSort
boxplot(`Time(Miliseconds)` ~ Array_Size, data = filtered_data, las = 1,
        ylab = "Time(Miliseconds)", xlab = "Array Size")


# Interaction plot containing all blocks
interaction.plot(Data$Array_Size, Data$Sorting_Algorithm, Data$"Time(Miliseconds)",
                 col = c("red", "blue", "green"),   # Colors for each treatment level
                 lty = 1,                           # Line type
                 type = "b",                        # Line and points
                 pch = c(19, 17, 15),               # Point shapes for each treatment
                 xlab = "Array_Size", ylab = "Time(Miliseconds)", 
                 main = "Interaction Plot: Array sizes and Sorting Algorithms")


# Interaction plot containing all blocks, not containing bubble sort
interaction.plot(filtered_data$Array_Size, filtered_data$Sorting_Algorithm, filtered_data$"Time(Miliseconds)",
                 col = c("red", "blue", "green"),   # Colors for each treatment level
                 lty = 1,                           # Line type
                 type = "b",                        # Line and points
                 pch = c(19, 17, 15),               # Point shapes for each treatment
                 xlab = "Array_Size", ylab = "Time(Miliseconds)", 
                 main = "Interaction Plot: Array sizes and Sorting Algorithms")



# Model Diagnostics

par(mfrow = c(2, 3))

plot(m1)


hist(resid(m1), main = "", las = 1, breaks = 10)
shapiro.test(resid(m1))







