// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Grid from '@mui/material/Grid';
import DeveloperBoardIcon from '@mui/icons-material/DeveloperBoard';
import { useTranslate } from 'react-admin';

import { LinesChart, ChartContainer } from '../../../common';
import {
    groupHistoryByLabels,
    calculateTimeToCpuPercentageSerie,
    getMaxChartValue,
    getMinChartValue,
} from '../metricUtils';

const CpuResourceAllocation = ({ data }): JSX.Element => {
    const translate = useTranslate();
    const yMax = getMaxChartValue(data);
    const yMin = getMinChartValue(data);

    return (
        <ChartContainer
            headingText='label.run.metrics.cpu.resource_title'
        >
            <LinesChart
                data={data}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.cpu.mips_label')} (${translate('label.run.metrics.cpu.mips_unit')})`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

const CpuUsagePercentage = ({ data }): JSX.Element => {
    const translate = useTranslate();
    const yMax = getMaxChartValue(data);
    const yMin = getMinChartValue(data);

    return (
        <ChartContainer
            headingText='label.run.metrics.cpu.usage_title'
            icon={DeveloperBoardIcon}
        >
            <LinesChart
                data={data}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.cpu.percentage')} (${translate('label.run.metrics.cpu.percentage_unit')})`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

const CpuUtilization = ({ data }): JSX.Element | null => {
    const translate = useTranslate();
    const groupedData = groupHistoryByLabels(
        data,
        'time',
        ['allocatedMips', 'requestedMips'],
        {
            allocatedMips: `${translate('label.run.metrics.cpu.allocated')} ${translate('label.run.metrics.cpu.mips_unit')}`,
            requestedMips: `${translate('label.run.metrics.cpu.requested')} ${translate('label.run.metrics.cpu.mips_unit')}`,
        },
    );
    const cpuPercentage = calculateTimeToCpuPercentageSerie(
        data,
        'time',
        translate('label.run.metrics.cpu.percentage'),
    );

    return (
        <Grid container>
            <Grid item md={6} sm={12} xs={12}>
                <CpuUsagePercentage data={[cpuPercentage]} />
            </Grid>
            <Grid item md={6} sm={12} xs={12}>
                <CpuResourceAllocation data={groupedData} />
            </Grid>
        </Grid>
    );
};

export default CpuUtilization;
