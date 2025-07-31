package com.stockinsights.stockapi.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `GET quote should return 200`() {
        mockMvc.get("/api/stocks/quote?symbol=AAPL")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET simulated candles should return 200`() {
        mockMvc.get("/api/stocks/candle/simulated?symbol=AAPL")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET top gainers should return 200`() {
        mockMvc.get("/api/stocks/top-gainers")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET top losers should return 200`() {
        mockMvc.get("/api/stocks/top-losers")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET trending stocks should return 200`() {
        mockMvc.get("/api/stocks/trending")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET Nifty stocks should return 200`() {
        mockMvc.get("/api/stocks/nifty")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET Sensex stocks should return 200`() {
        mockMvc.get("/api/stocks/sensex")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET historical candle data should return 200`() {
        val from = System.currentTimeMillis() / 1000 - 86400
        val to = System.currentTimeMillis() / 1000
        mockMvc.get("/api/stocks/candle?symbol=AAPL&resolution=D&from=$from&to=$to")
            .andExpect {
                status { isOk() }
            }
    }
}
