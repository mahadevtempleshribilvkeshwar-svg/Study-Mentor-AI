package com.example.data.repository

import com.example.data.model.GameTransactionScenario
import com.example.data.model.VirtualBusiness

object GameRepository {

    val availableBusinesses = listOf(
        VirtualBusiness(
            id = "biz_mobile",
            name = "TechZone Mobile Hub",
            typeName = "Consumer Electronics Retailer",
            description = "Manage sales of smartphones, trade discounts, credit purchases, warranty claims and GST entries.",
            iconEmoji = "📱",
            cashBalance = 150000.0,
            bankBalance = 420000.0,
            totalRevenue = 890000.0,
            totalExpense = 610000.0,
            levelRequired = 1
        ),
        VirtualBusiness(
            id = "biz_computers",
            name = "ByteCrafters IT & Hardware",
            typeName = "Computer & Server Solutions",
            description = "Handle capital investments, office equipment depreciation, client receivables, bad debts & AMC contracts.",
            iconEmoji = "💻",
            cashBalance = 90000.0,
            bankBalance = 650000.0,
            totalRevenue = 1240000.0,
            totalExpense = 820000.0,
            levelRequired = 1
        ),
        VirtualBusiness(
            id = "biz_clothing",
            name = "FabThread Apparels & Co.",
            typeName = "Fashion Garments & Boutique",
            description = "Handle supplier trade discounts, returns inward, returns outward, inventory valuation and drawings in goods.",
            iconEmoji = "👗",
            cashBalance = 75000.0,
            bankBalance = 380000.0,
            totalRevenue = 670000.0,
            totalExpense = 430000.0,
            levelRequired = 2
        ),
        VirtualBusiness(
            id = "biz_restaurant",
            name = "SpiceDelight Bistro & Cafe",
            typeName = "Food & Hospitality Enterprise",
            description = "Manage daily cash collections, perishable stock write-offs, prepaid lease expenses and catering advances.",
            iconEmoji = "☕",
            cashBalance = 110000.0,
            bankBalance = 290000.0,
            totalRevenue = 540000.0,
            totalExpense = 390000.0,
            levelRequired = 2
        ),
        VirtualBusiness(
            id = "biz_supermarket",
            name = "GreenMart Daily Superstore",
            typeName = "FMCG Grocery & Retail Chain",
            description = "Record high-volume counter sales, cash discounts allowed/received, bank overdraft interest & supplier bills.",
            iconEmoji = "🛒",
            cashBalance = 180000.0,
            bankBalance = 510000.0,
            totalRevenue = 1820000.0,
            totalExpense = 1450000.0,
            levelRequired = 3
        ),
        VirtualBusiness(
            id = "biz_partnership",
            name = "Apex Global Partners",
            typeName = "Class 12 Partnership Firm (A, B & C)",
            description = "Solve partner admissions, premium for goodwill, revaluation of assets, interest on capital & profit distribution.",
            iconEmoji = "🤝",
            cashBalance = 250000.0,
            bankBalance = 1200000.0,
            totalRevenue = 2900000.0,
            totalExpense = 1950000.0,
            levelRequired = 3
        )
    )

    val gameScenarios: Map<String, List<GameTransactionScenario>> = mapOf(
        "biz_mobile" to listOf(
            GameTransactionScenario(
                id = "sc_mob_1",
                businessId = "biz_mobile",
                title = "Business Commenced with Capital",
                transactionText = "Owner commenced 'TechZone Mobile Hub' by investing ₹1,50,000 in Cash and ₹3,50,000 via Bank transfer.",
                date = "2026-04-01",
                contextInfo = "Starting business operations. Both Cash & Bank are incoming assets, balanced by Owner's Capital.",
                expectedDebitAccount = "Cash A/c & Bank A/c",
                expectedCreditAccount = "Capital A/c",
                expectedAmount = 500000.0,
                expectedNarration = "Being capital introduced in cash and bank",
                ruleApplied = "Modern Rule: Assets increase (Dr Cash ₹1,50,000, Dr Bank ₹3,50,000), Capital increases (Cr Capital ₹5,00,000)",
                xpReward = 60,
                coinReward = 25,
                explanation = "Debit what comes in (Cash & Bank increases assets). Credit the giver / Capital increases by ₹5,00,000."
            ),
            GameTransactionScenario(
                id = "sc_mob_2",
                businessId = "biz_mobile",
                title = "Credit Purchase with Trade Discount",
                transactionText = "Purchased 10 flagship 5G Smartphones with catalogue price of ₹2,00,000 from Apex Distributing Co. at 10% Trade Discount on credit.",
                date = "2026-04-05",
                contextInfo = "Important Board Rule: Trade Discount is NEVER recorded in the books of accounts; it is deducted directly from list price!",
                expectedDebitAccount = "Purchases A/c",
                expectedCreditAccount = "Apex Distributing Co. A/c",
                expectedAmount = 180000.0,
                expectedNarration = "Being goods purchased on credit at 10% trade discount",
                ruleApplied = "Modern Rule: Purchases (Expense/Asset) increases (Dr), Creditor (Liability) increases (Cr) at Net Price = ₹2,00,000 - 10% = ₹1,80,000.",
                xpReward = 75,
                coinReward = 30,
                explanation = "List Price = ₹2,00,000. Trade Discount @ 10% = ₹20,000. Net Invoice Amount = ₹1,80,000. Trade discount is not shown separately."
            ),
            GameTransactionScenario(
                id = "sc_mob_3",
                businessId = "biz_mobile",
                title = "Payment to Creditor with Cash Discount",
                transactionText = "Paid Apex Distributing Co. ₹1,76,400 by Cheque in full and final settlement of their account (₹1,80,000), availing 2% Cash Discount.",
                date = "2026-04-12",
                contextInfo = "Cash discount is an income for the buyer (Discount Received) and IS recorded in the books!",
                expectedDebitAccount = "Apex Distributing Co. A/c",
                expectedCreditAccount = "Bank A/c & Discount Received A/c",
                expectedAmount = 180000.0,
                expectedNarration = "Being payment made to creditor and cash discount received",
                ruleApplied = "Liability reduced (Dr Creditor ₹1,80,000), Bank reduced (Cr Bank ₹1,76,400), Gain/Revenue earned (Cr Discount Received ₹3,600).",
                xpReward = 80,
                coinReward = 35,
                explanation = "Debit the Receiver (Apex Distributing Co. ₹1,80,000). Credit Bank (what goes out ₹1,76,400) and Credit Discount Received (Nominal: gain ₹3,600)."
            ),
            GameTransactionScenario(
                id = "sc_mob_4",
                businessId = "biz_mobile",
                title = "Goods withdrawn for Personal Use",
                transactionText = "Proprietor took one smartphone costing ₹18,000 (selling price ₹22,000) for personal family gift.",
                date = "2026-04-20",
                contextInfo = "When proprietor takes goods for domestic use, it is recorded at COST PRICE by reducing Purchases.",
                expectedDebitAccount = "Drawings A/c",
                expectedCreditAccount = "Purchases A/c",
                expectedAmount = 180000.0, // 18000
                expectedNarration = "Being goods withdrawn for personal use at cost",
                ruleApplied = "Debit Drawings (reduces capital), Credit Purchases (reduces cost of goods available for sale at cost price ₹18,000).",
                xpReward = 70,
                coinReward = 30,
                explanation = "Drawings A/c Dr. ₹18,000 To Purchases A/c ₹18,000. Note: Never credit Sales A/c, because no profit was realized!"
            )
        ),
        "biz_partnership" to listOf(
            GameTransactionScenario(
                id = "sc_part_1",
                businessId = "biz_partnership",
                title = "Admission of Partner & Goodwill Premium",
                transactionText = "Admitted Charlie as 1/5th partner. Charlie brings ₹3,00,000 as Capital and ₹60,000 as his share of Premium for Goodwill in cash.",
                date = "2026-05-01",
                contextInfo = "Partners Alice and Bob share profits equally. The goodwill premium must be shared between Alice and Bob in their sacrificing ratio.",
                expectedDebitAccount = "Bank A/c",
                expectedCreditAccount = "Charlie's Capital A/c & Premium for Goodwill A/c",
                expectedAmount = 360000.0,
                expectedNarration = "Being capital and premium for goodwill brought in by Charlie",
                ruleApplied = "Bank A/c Dr. ₹3,60,000 To Charlie's Capital A/c ₹3,00,000 To Premium for Goodwill A/c ₹60,000.",
                xpReward = 100,
                coinReward = 45,
                explanation = "Asset increases (Dr Bank ₹3,60,000). Charlie's Capital increases (Cr ₹3,00,000) and Goodwill Premium recorded (Cr ₹60,000), which is then credited to Alice & Bob ₹30,000 each in sacrificing ratio."
            ),
            GameTransactionScenario(
                id = "sc_part_2",
                businessId = "biz_partnership",
                title = "Unrecorded Asset Revaluation",
                transactionText = "An unrecorded laser printer valued at ₹15,000 was discovered in the office and brought into books upon reconstitution.",
                date = "2026-05-02",
                contextInfo = "Revaluation of unrecorded assets represents a gain for the firm before admission.",
                expectedDebitAccount = "Office Equipment / Printer A/c",
                expectedCreditAccount = "Revaluation A/c",
                expectedAmount = 15000.0,
                expectedNarration = "Being unrecorded asset recorded on reconstitution",
                ruleApplied = "Asset increases (Dr Office Equipment ₹15,000), Gain on Revaluation (Cr Revaluation A/c ₹15,000).",
                xpReward = 80,
                coinReward = 35,
                explanation = "Debit asset created (Office Equipment Dr. ₹15,000). Credit Revaluation A/c (Nominal gain on revaluation)."
            ),
            GameTransactionScenario(
                id = "sc_part_3",
                businessId = "biz_partnership",
                title = "Provision for Doubtful Debts Created",
                transactionText = "Maintain a provision for doubtful debts at 5% on Sundry Debtors book value of ₹1,20,000.",
                date = "2026-05-03",
                contextInfo = "Provision for doubtful debts reduces asset realizable value and represents a loss on revaluation.",
                expectedDebitAccount = "Revaluation A/c",
                expectedCreditAccount = "Provision for Doubtful Debts A/c",
                expectedAmount = 6000.0,
                expectedNarration = "Being 5% provision created for doubtful debts",
                ruleApplied = "Revaluation loss (Dr Revaluation A/c ₹6,000), Provision liability/contra-asset (Cr Provision for Doubtful Debts ₹6,000).",
                xpReward = 85,
                coinReward = 40,
                explanation = "5% of ₹1,20,000 = ₹6,000. Revaluation A/c Dr. ₹6,000 To Provision for Doubtful Debts A/c ₹6,000."
            )
        ),
        "biz_computers" to listOf(
            GameTransactionScenario(
                id = "sc_comp_1",
                businessId = "biz_computers",
                title = "Purchase of Machinery & Installation Wages",
                transactionText = "Purchased Server Equipment for ₹1,80,000 by cheque and paid ₹12,000 in cash for installation & testing.",
                date = "2026-04-10",
                contextInfo = "Capital Expenditure Rule: All expenses incurred to bring an asset into working condition are added to asset cost!",
                expectedDebitAccount = "Server Equipment / Machinery A/c",
                expectedCreditAccount = "Bank A/c & Cash A/c",
                expectedAmount = 192000.0,
                expectedNarration = "Being equipment purchased and installation wages capitalized",
                ruleApplied = "Asset increases by total capitalized cost (Dr Server Equipment ₹1,92,000), Bank reduces (Cr ₹1,80,000), Cash reduces (Cr ₹12,000).",
                xpReward = 90,
                coinReward = 40,
                explanation = "Installation cost is capital expenditure. Do NOT debit Wages A/c! Debit Server Equipment A/c ₹1,92,000."
            )
        )
    )

    fun getScenariosForBusiness(businessId: String): List<GameTransactionScenario> {
        return gameScenarios[businessId] ?: gameScenarios["biz_mobile"] ?: emptyList()
    }
}
