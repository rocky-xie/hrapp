import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { ApplicationStage } from '@/shared/model/application-stage.model';

import ApplicationStageService from './application-stage.service';

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
  describe('ApplicationStage Service', () => {
    let service: ApplicationStageService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ApplicationStageService();
      currentDate = new Date();
      elemDefault = new ApplicationStage(123, 'RESUME_SCREENING', 0, 'AAAAAAA', currentDate, currentDate, false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a ApplicationStage', async () => {
        const returnedFromService = {
          id: 123,
          startedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { startedAt: currentDate, completedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ApplicationStage', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ApplicationStage', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          sequenceNumber: 1,
          stageStatus: 'BBBBBB',
          startedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          selected: true,
          decisionReason: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startedAt: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ApplicationStage', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ApplicationStage', async () => {
        const patchObject = {
          stageType: 'BBBBBB',
          stageStatus: 'BBBBBB',
          startedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          selected: true,
          decisionReason: 'BBBBBB',
          ...new ApplicationStage(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startedAt: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ApplicationStage', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ApplicationStage', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          sequenceNumber: 1,
          stageStatus: 'BBBBBB',
          startedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          selected: true,
          decisionReason: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startedAt: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ApplicationStage', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ApplicationStage', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ApplicationStage', async () => {
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
