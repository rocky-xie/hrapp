import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { ImprovementPlan } from '@/shared/model/improvement-plan.model';

import ImprovementPlanService from './improvement-plan.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('ImprovementPlan Service', () => {
    let service: ImprovementPlanService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ImprovementPlanService();
      currentDate = new Date();
      elemDefault = new ImprovementPlan(
        123,
        'AAAAAAA',
        'DRAFT',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a ImprovementPlan', async () => {
        const returnedFromService = {
          id: 123,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { startDate: currentDate, targetDate: currentDate, completionDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ImprovementPlan', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ImprovementPlan', async () => {
        const returnedFromService = {
          planName: 'BBBBBB',
          planStatus: 'BBBBBB',
          problemSummary: 'BBBBBB',
          improvementAction: 'BBBBBB',
          ownerName: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          reviewResult: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDate: currentDate, targetDate: currentDate, completionDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ImprovementPlan', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ImprovementPlan', async () => {
        const patchObject = {
          planName: 'BBBBBB',
          planStatus: 'BBBBBB',
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          ...new ImprovementPlan(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDate: currentDate, targetDate: currentDate, completionDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ImprovementPlan', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ImprovementPlan', async () => {
        const returnedFromService = {
          planName: 'BBBBBB',
          planStatus: 'BBBBBB',
          problemSummary: 'BBBBBB',
          improvementAction: 'BBBBBB',
          ownerName: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          reviewResult: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDate: currentDate, targetDate: currentDate, completionDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ImprovementPlan', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ImprovementPlan', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ImprovementPlan', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
