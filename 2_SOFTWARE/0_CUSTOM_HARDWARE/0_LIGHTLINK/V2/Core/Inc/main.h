/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.h
  * @brief          : Header for main.c file.
  *                   This file contains the common defines of the application.
  ******************************************************************************
  * @attention
  *
  * Copyright (c) 2024 STMicroelectronics.
  * All rights reserved.
  *
  * This software is licensed under terms that can be found in the LICENSE file
  * in the root directory of this software component.
  * If no LICENSE file comes with this software, it is provided AS-IS.
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "stm32h7xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */

/* USER CODE END EM */

void HAL_TIM_MspPostInit(TIM_HandleTypeDef *htim);

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
#define R_LED_Pin GPIO_PIN_0
#define R_LED_GPIO_Port GPIOC
#define G_LED_Pin GPIO_PIN_1
#define G_LED_GPIO_Port GPIOC
#define M1_TRIG_Pin GPIO_PIN_4
#define M1_TRIG_GPIO_Port GPIOC
#define M12_STAT_Pin GPIO_PIN_5
#define M12_STAT_GPIO_Port GPIOC
#define M2_TRIG_Pin GPIO_PIN_0
#define M2_TRIG_GPIO_Port GPIOB
#define M3_TRIG_Pin GPIO_PIN_1
#define M3_TRIG_GPIO_Port GPIOB
#define M34_STAT_Pin GPIO_PIN_2
#define M34_STAT_GPIO_Port GPIOB
#define M4_TRIG_Pin GPIO_PIN_7
#define M4_TRIG_GPIO_Port GPIOE
#define M5_TRIG_Pin GPIO_PIN_8
#define M5_TRIG_GPIO_Port GPIOE
#define M56_STAT_Pin GPIO_PIN_9
#define M56_STAT_GPIO_Port GPIOE
#define M6_TRIG_Pin GPIO_PIN_10
#define M6_TRIG_GPIO_Port GPIOE
#define M7_TRIG_Pin GPIO_PIN_15
#define M7_TRIG_GPIO_Port GPIOE
#define M78_STAT_Pin GPIO_PIN_10
#define M78_STAT_GPIO_Port GPIOB
#define M8_TRIG_Pin GPIO_PIN_11
#define M8_TRIG_GPIO_Port GPIOB
#define M9_TRIG_Pin GPIO_PIN_8
#define M9_TRIG_GPIO_Port GPIOD
#define M910_STAT_Pin GPIO_PIN_9
#define M910_STAT_GPIO_Port GPIOD
#define M10_TRIG_Pin GPIO_PIN_10
#define M10_TRIG_GPIO_Port GPIOD
#define M11_TRIG_Pin GPIO_PIN_11
#define M11_TRIG_GPIO_Port GPIOD
#define M1112_STAT_Pin GPIO_PIN_12
#define M1112_STAT_GPIO_Port GPIOD
#define M12_TRIG_Pin GPIO_PIN_13
#define M12_TRIG_GPIO_Port GPIOD
#define nBUZZ_Pin GPIO_PIN_14
#define nBUZZ_GPIO_Port GPIOD
#define INH_12V_Pin GPIO_PIN_15
#define INH_12V_GPIO_Port GPIOD
#define ADDR_6_Pin GPIO_PIN_6
#define ADDR_6_GPIO_Port GPIOC
#define ADDR_5_Pin GPIO_PIN_7
#define ADDR_5_GPIO_Port GPIOC
#define ADDR_4_Pin GPIO_PIN_8
#define ADDR_4_GPIO_Port GPIOC
#define ADDR_3_Pin GPIO_PIN_9
#define ADDR_3_GPIO_Port GPIOC
#define ADDR_2_Pin GPIO_PIN_8
#define ADDR_2_GPIO_Port GPIOA
#define ADDR_1_Pin GPIO_PIN_9
#define ADDR_1_GPIO_Port GPIOA
#define DIAG_ADC_nCS_Pin GPIO_PIN_15
#define DIAG_ADC_nCS_GPIO_Port GPIOA
#define TCAN1146_nCS_Pin GPIO_PIN_4
#define TCAN1146_nCS_GPIO_Port GPIOD

/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */
